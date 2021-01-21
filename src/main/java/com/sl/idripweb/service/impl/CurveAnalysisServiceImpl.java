package com.sl.idripweb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sl.common.config.CurveColor;
import com.sl.common.config.TgHistoryRelation;
import com.sl.common.utils.DateUtil;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.dao.CurveAnalysisDao;
import com.sl.idripweb.service.CurveAnalysisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CurveAnalysisServiceImpl implements CurveAnalysisService {

    @Autowired
    private CurveAnalysisDao curveAnalysisDao;
    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private CurveColor curveColor;
    @Autowired
    private TgHistoryRelation tgHistoryRelation;

    //获取配电室/水泵房/调压站下meter列表
    @Override
    public WebResult getRoomDevice(Map<String, Object> map) {
        initParams(map);
        ArrayList<Map<String, Object>> list = curveAnalysisDao.getRoomDevice(map);
        Map<String, Object> result = new HashMap<>();
        result.put("data_list", list);
        return WebResult.success(result);
    }

    //获取配电室低压进线表
    @Override
    public WebResult getLowIncomingLine(Map<String, Object> map) {
        int roomId = Integer.parseInt(String.valueOf(map.get("room_id")));
        if(roomId == 0){
            return WebResult.error(404);
        }
        ArrayList<Map<String, Object>> lowIncomingLine = curveAnalysisDao.getLowIncomingLine(map);
        return WebResult.success(lowIncomingLine);
    }

    //页面数据
    @Override
    public WebResult index(HttpServletRequest request, Map<String, Object> map) {
        HttpSession session = request.getSession();
        //处理前端传递过来的数组参数问题
        //遍历所有参数判断key如果包含params_arr就在map中将key-value放进去
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        Iterator<Map.Entry<String, Object>> mapIterator = entrySet.iterator();
        List<String> paramsArr = JSONArray.parseArray(String.valueOf(map.get("params_arr")), String.class);// ["uPhase","uLine","I","va","vb","vc","ia","ib","ic"]
        List<String> thdNumArr = JSONArray.parseArray(String.valueOf(map.get("thdNum_arr")), String.class);
        paramsArr = paramsArr == null ? new ArrayList<>() : paramsArr;
        thdNumArr = thdNumArr == null ? new ArrayList<>() : thdNumArr;
        while (mapIterator.hasNext()){
            Map.Entry<String, Object> next = mapIterator.next();
            String key = next.getKey();
            String value = String.valueOf(next.getValue());
            if(key.contains("params_arr") && !paramsArr.contains(value)){
                paramsArr.add(value);
            }
            if(key.contains("thdNum_arr") && !thdNumArr.contains(value)){
                paramsArr.add(value);
            }
        }
        //结果集存储
        Map<String, Object> result = new HashMap<>();
        String projectId = map.get("project_id") != null ? String.valueOf(map.get("project_id")) : null;
        String factoryId = map.get("factory_id") != null ? String.valueOf(map.get("factory_id")) : null;
        if(StringUtils.isEmpty(projectId)){
           projectId = String.valueOf(session.getAttribute("pid"));
        }
        if(StringUtils.isEmpty(factoryId)){
            factoryId = String.valueOf(session.getAttribute("factory_id"));
        }
        String transformerRoomId = String.valueOf(map.get("room_id"));
        String deviceId = String.valueOf(map.get("device_id"));
        String deviceName = String.valueOf(map.get("device_name"));
        String bTime = String.valueOf(map.get("date_from"));
        String eTime = String.valueOf(map.get("date_to"));


        if(StringUtils.isEmpty(deviceId) || StringUtils.isEmpty(deviceName)){
            return WebResult.error(404);
        }
        // 默认返回今日数据
        if(!StringUtils.isEmpty(bTime)){
            bTime += ":00";
        }else {
            bTime = dateUtil.getTodayZeroStr();
        }
        if(!StringUtils.isEmpty(eTime)){
            eTime += ":00";
        }else {
            eTime = dateUtil.getTodayStr();
        }
        long period = (dateUtil.str2TimeStamp(eTime, null) - dateUtil.str2TimeStamp(bTime, null))/1000;
        if(period > 86400){
            return WebResult.error(404);
        }

        // 默认返回U相数据
        if(paramsArr == null || paramsArr.size() == 0){
            paramsArr = new ArrayList<>();
            paramsArr.add("uPhase");
        }
        // 获取通讯机
        List<String> tgInfo = new ArrayList<>();
        JSONArray tg = JSONArray.parseArray(JSON.toJSONString(session.getAttribute("tg_info")));
        for(int i=0;i<tg.size();i++){
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(tg.get(i)));
            String fId = String.valueOf(jsonObject.get("factory_id"));
            if(fId.equals(factoryId)){
                tgInfo.add(String.valueOf(jsonObject.get("TG_RTDB")));
            }
        }

        // 构建要查询的标签数组
        List<String> tgList = makeTag(paramsArr, thdNumArr);

        // 获取ep数据
        List<String> mongo_tags = new ArrayList<>();
        List<String> mysql_tags = new ArrayList<>();

        // 获取ep表读数（MongoDB）
        List<String> tagStrList = Arrays.asList(("ep,qf,gqf").split(","));
        for(String t : tgList){
            if(tagStrList.indexOf(t) >= 0){
                mongo_tags.add(t);
            }else{
                mysql_tags.add(t);
            }
        }
        // 获取标签颜色值
        Map<String, String> cColor = curveColor.getColor();
        List<String> legend1 = new ArrayList<>();
        List<String> legend2 = new ArrayList<>();
        List<String> bph = Arrays.asList(("ibph,ubph").split(","));
        long bTimeStamp = dateUtil.str2TimeStamp(bTime, "yyyy-MM-dd HH:mm:ss");
        long eTimeStamp = dateUtil.str2TimeStamp(eTime, "yyyy-MM-dd HH:mm:ss");

        Map<String, Map<String, Object>> chartMap = new HashMap<>();
        Map<String, Map<String, Object>> chart1 = new HashMap<>();
        Map<String, Map<String, Object>> chart2 = new HashMap<>();

        // 构建X轴数组
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        long btime_tmp = Long.parseLong(nf.format(Math.floor((bTimeStamp / 300000) + 1) * 300000));
        List<String> xAxis = new ArrayList<>();
        for(long i=btime_tmp;i<eTimeStamp-1;i+=300000){
            xAxis.add(dateUtil.parseTimestampToStr(i, "yyyy-MM-dd HH:mm:00"));
        }


        Map<String, List<Map>> data_list = new HashMap<>();
        Map<String, List<String>> tag_table_relation = new HashMap<>();
        if(!(mysql_tags.size() == 0)){
            // 查找标签对应历史数据表
            String start_date = dateUtil.parseStrToStr(bTime, "yyyy-MM-dd");
            String end_date = dateUtil.parseStrToStr(eTime, "yyyy-MM-dd");
            if(mysql_tags.size() > 0){
                for(String each : mysql_tags){
                    String tableName = tgHistoryRelation.getElec().get(each);
                    if(!StringUtils.isEmpty(tableName)){
                        if(tag_table_relation.get(tableName) == null)
                            tag_table_relation.put(tableName, new ArrayList<>());
                        tag_table_relation.get(tableName).add(each);
                    }
                }
            }

            // 按表查找对应数据
            Set<Map.Entry<String, List<String>>> entries = tag_table_relation.entrySet();
            Iterator<Map.Entry<String, List<String>>> iterator = entries.iterator();
            while (iterator.hasNext()){
                Map.Entry<String, List<String>> next = iterator.next();
                String key = next.getKey();
                List<String> value = next.getValue();
                Map<String, Object> params = new HashMap<>();
                params.put("pname", session.getAttribute("pname"));
                params.put("devices", Arrays.asList(deviceName.split(",")));
                params.put("tag_type", "elec");
                params.put("order", "asc");
                params.put("end_time", end_date);
                params.put("start_time", start_date);
                params.put("key", key);
                params.put("value", value);
                ArrayList<Map<String, Object>> historyData = curveAnalysisDao.getHistoryData(params);
                Map<String, List<String>> dataList = new HashMap<>();
                for(Map<String, Object> each : historyData){
                    List<Map> detail = new ArrayList<>();
                    if(each.get("detail") != null) {
                        detail = JSONArray.parseArray(String.valueOf(each.get("detail")), Map.class);
                    }
                    Collections.sort(detail, new Comparator<Map>() {
                        @Override
                        public int compare(Map e1, Map e2) {
                            return (int)(dateUtil.str2TimeStamp(String.valueOf(e1.get("log_time")), "yyyy-MM-dd HH:mm:ss") - dateUtil.str2TimeStamp(String.valueOf(e2.get("log_time")), "yyyy-MM-dd HH:mm:ss"));
                        }
                    });
                    // 增加属性，保持与MongoDB数据一致
                    String device_name = String.valueOf(each.get("device_name"));
                    String tag_name = String.valueOf(each.get("tag_name"));

                    //构建图例；将数据按标签分类
                    if(!bph.contains(tag_name)){
                        if(!(legend1.contains(tag_name))) legend1.add(tag_name);
                        chartMap = chart1;
                    }else{
                        if(!(legend2.contains(tag_name))) legend2.add(tag_name);
                        chartMap = chart2;
                    }
                    if(chartMap.get(tag_name) == null)
                        chartMap.put(tag_name, new HashMap<>());
                    chartMap.get(tag_name).put("name", tag_name);
                    chartMap.get(tag_name).put("color", cColor.get(tag_name));
                    if(dataList.get(tag_name) == null)
                        dataList.put(tag_name, new ArrayList<>());
                    for(int i=0;i<detail.size();i++){
                        detail.get(i).put("device_name", device_name);
                        detail.get(i).put("tag", tag_name);
                        long log_time = dateUtil.str2TimeStamp(String.valueOf(detail.get(i).get("log_time")), "yyyy-MM-dd HH:mm:ss");
                        String newLogTime = dateUtil.parseTimestampToStr(log_time, "yyyy-MM-dd HH:mm:00");
                        if(log_time >= bTimeStamp && log_time <= eTimeStamp){
                            //修复查询时间为准点时索引异常问题
                            if(dataList.get(tag_name).size() == 0 && new Date(log_time).getMinutes() != dateUtil.str2Date(xAxis.get(dataList.get(tag_name).size()), "yyyy-MM-dd HH:mm:ss").getMinutes()) continue;
                            if(!newLogTime.equals(xAxis.get(dataList.get(tag_name).size())))
                                dataList.get(tag_name).add("");
                            else
                                dataList.get(tag_name).add(String.valueOf(detail.get(i).get("val")));
                        }

                    }

                    chartMap.get(tag_name).put("data", new ArrayList<>(dataList.get(tag_name)));

                    if(data_list.get(tag_name) == null)
                        data_list.put(tag_name, new ArrayList<>());
                    data_list.get(tag_name).addAll(detail);
                }
            }
        }

        // 获取标线数据
        ArrayList<Map<String, Object>> markLineTmp = curveAnalysisDao.getThreshold(deviceId);
        Map<String, ArrayList<Map<String, Map<String, Object>>>> markLine = new HashMap<>();
        for(Map<String, Object> each : markLineTmp){
            String tag = String.valueOf(each.get("tag"));
            if(tag.contains("u") && markLine.get("u") != null){
                ArrayList<Map<String, Map<String, Object>>> markLineListU = new ArrayList<>();
                Map<String, Map<String, Object>> level = new HashMap<>();
                Map<String, Object> levelMap = new HashMap<>();
                levelMap.put("name", "u:低限");
                levelMap.put("value", each.get("l_level1"));
                levelMap.put("color", cColor.get("u-l_level1"));
                level.put("l_level1", levelMap);
                markLineListU.add(level);

                level = new HashMap<>();
                levelMap = new HashMap<>();
                levelMap.put("name", "u:低低限");
                levelMap.put("value", each.get("l_level2"));
                levelMap.put("color", cColor.get("u-l_level2"));
                level.put("l_level2", levelMap);
                markLineListU.add(level);

                level = new HashMap<>();
                levelMap = new HashMap<>();
                levelMap.put("name", "u:高限");
                levelMap.put("value", each.get("h_level1"));
                levelMap.put("color", cColor.get("u-h_level1"));
                level.put("h_level1", levelMap);
                markLineListU.add(level);

                level = new HashMap<>();
                levelMap = new HashMap<>();
                levelMap.put("name", "u:高高限");
                levelMap.put("value", each.get("h_level2"));
                levelMap.put("color", cColor.get("u-h_level2"));
                level.put("h_level2", levelMap);
                markLineListU.add(level);
                markLine.put("u", markLineListU);

            }

            if(tag.contains("i") && markLine.get("i") != null){
                ArrayList<Map<String, Map<String, Object>>> markLineListI = new ArrayList<>();
                Map<String, Map<String, Object>> level = new HashMap<>();
                Map<String, Object> levelMap = new HashMap<>();
                levelMap.put("name", "i:高限");
                levelMap.put("value", each.get("'h_level1'"));
                levelMap.put("color", cColor.get("i-h_level1"));
                level.put("'h_level1'", levelMap);
                markLineListI.add(level);

                level = new HashMap<>();
                levelMap = new HashMap<>();
                levelMap.put("name", "i:高高限");
                levelMap.put("value", each.get("h_level2"));
                levelMap.put("color", cColor.get("i-h_level2"));
                level.put("h_level2", levelMap);
                markLineListI.add(level);
                markLine.put("i", markLineListI);
            }
        }
        //最大值，最小值，平均值
        Map<String, Object> res1 = new HashMap<>();
        Map<String, Object> res2 = new HashMap<>();
        setDataCompare(chart1, res1);
        setDataCompare(chart2, res2);

        res1.put("legend", legend1);
        res1.put("xAxis", xAxis);
        res1.put("data", chart1);

        res2.put("legend", legend2);
        res2.put("xAxis", xAxis);
        res2.put("data", chart2);
        Map<String, Object> res = new HashMap<>();
        res.put("res1", res1);// U线/U相/I...chart数据
        res.put("res2", res2);// 三项不平衡的chart数据
        res.put("mark_line", markLine);// u/i标准线

        result.put("chart_list", res);
        return WebResult.success(result);
    }

    //设置返回值中的最大值和最小值
    private void setDataCompare(Map<String, Map<String, Object>> chart, Map<String, Object> res) {
        Set<Map.Entry<String, Map<String, Object>>> entries = chart.entrySet();
        Iterator<Map.Entry<String, Map<String, Object>>> iterator = entries.iterator();
        Map<String, Map<String, Object>> dataCompareMap = new HashMap<>();
        dataCompareMap.put("avg", new HashMap<>());
        dataCompareMap.put("min", new HashMap<>());
        dataCompareMap.put("max", new HashMap<>());
        while (iterator.hasNext()){
            Map.Entry<String, Map<String, Object>> next = iterator.next();
            String key = next.getKey();
            dataCompareMap.computeIfAbsent(key, k -> new HashMap<>());
            List<Double> data = JSONArray.parseArray(String.valueOf(next.getValue().get("data")), Double.class);
            Double avg = data.stream().collect(Collectors.averagingDouble(Double::doubleValue));
            Double min = data.size() > 0 ? data.stream().min(Double::compareTo).get() : 0.0;
            Double max = data.size() > 0 ? data.stream().max(Double::compareTo).get() : 0.0;
            dataCompareMap.get("avg").put(key, (double)Math.round(avg*100)/100);
            dataCompareMap.get("min").put(key, min);
            dataCompareMap.get("max").put(key, max);
        }
        res.put("avg", dataCompareMap.get("avg"));
        res.put("min", dataCompareMap.get("min"));
        res.put("max", dataCompareMap.get("max"));
    }

    // 构建要查询的标签数组
    private List<String> makeTag(List<String> paramsArr, List<String> thdNumArr) {
        List<String> result = new ArrayList<>();
        String allParamsStr = "uPhase,uLine,I,ia,ib,ic,va,vb,vc";
        List<String> allParams = new ArrayList(Arrays.asList(allParamsStr.split(",")));
        // 具体化电标签
        if(paramsArr != null && paramsArr.size() > 0){
            List<String> tempParamsArr = new ArrayList<>(paramsArr);
            tempParamsArr.removeAll(allParams);
            result = new ArrayList<>(tempParamsArr);
            if(paramsArr.contains("uLine")){
                String uLine = "uab,ubc,uca";
                List<String> uLineList = Arrays.asList(uLine.split(","));
                result.removeAll(uLineList);
                result.addAll(uLineList);
            }

            if(paramsArr.contains("uPhase")){
                String uPhase = "ua,ub,uc,ubph";
                List<String> uPhaseList = Arrays.asList(uPhase.split(","));
                result.removeAll(uPhaseList);
                result.addAll(uPhaseList);
            }

            if(paramsArr.contains("I")){
                String I = "ia,ib,ic,ibph";
                List<String> IList = Arrays.asList(I.split(","));
                result.removeAll(IList);
                result.addAll(IList);
            }

        }

        // 根据谐波次数构建谐波标签
        if(thdNumArr != null && thdNumArr.size() > 0){
            List<String> tmp = new ArrayList<>();
            if(paramsArr.contains("ia")){
                tmp.add("thd-ia");
            }
            if(paramsArr.contains("ib")){
                tmp.add("thd-ib");
            }
            if(paramsArr.contains("ic")){
                tmp.add("thd-ic");
            }
            if(paramsArr.contains("va")){
                tmp.add("thd-va");
            }
            if(paramsArr.contains("vb")){
                tmp.add("thd-vb");
            }
            if(paramsArr.contains("vc")){
                tmp.add("thd-vc");
            }
            for(String val : thdNumArr){
                for(String v : tmp){
                    // 谐波总量
                    if("0".equals(val)){
                        if(!result.contains(v)) result.add(v);
                    }else{
                        if(!result.contains(v + val)) result.add(v + val);
                    }
                }
            }
        }
        return result;
    }

    //根据类型初始化参数
    private void initParams(Map<String, Object> map) {
        String roomType = String.valueOf(map.get("room_type"));
        switch (roomType) {
            case "2":
                map.put("room_name", "water_pump_room");
                map.put("meter_type", "water");
                break;
            case "3":
                map.put("room_name", "gas_pressure_room");
                map.put("meter_type", "gas");
                break;
            case "1" :
            default:
                map.put("room_name", "transformerroom");
                map.put("meter_type", "elec");
        }
    }

}
