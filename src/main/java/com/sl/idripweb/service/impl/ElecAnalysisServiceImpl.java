package com.sl.idripweb.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.sl.common.utils.DateUtil;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.dao.ElecAnalysisDao;
import com.sl.idripweb.entity.elecAnalysis.CategoryMeterData;
import com.sl.idripweb.service.ElecAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("elecAnalysisServiceImpl")
public class ElecAnalysisServiceImpl implements ElecAnalysisService {

    @Autowired
    private ElecAnalysisDao elecAnalysisDao;

    /**
     * 用电/用水/用气分析
     *
     * @param map
     * @return
     */
    @Override
    public WebResult index(Map<String, Object> map) {
        if (map.get("room_type") == null||map.get("date_type") == null||map.get("category_type") == null) {
            return WebResult.error(301);
        }
        if(map.get("date") == null||String.valueOf(map.get("date")).equals("")){
            map.put("date",DateUtil.parseDateToStr(new Date(),DateUtil.DATE_FORMAT_YYYY_MM_DD));
        }

        if (map.get("category_id")==null||String.valueOf(map.get("category_id")).equals("")){
            map.put("category_id",0);
        }
        switch (String.valueOf(map.get("room_type"))) {
            case "1":
                map.put("titleType","电");
                map.put("tag","ep");
                map.put("titleUnit","(kWh)");
                map.put("categoryRelationTable", "elec_project_category_relation");  // category
                map.put("meterRelationTable", "elec_category_meter_relation");  // category_meter
                map.put("roomIdName", "transformerroom_id");  // 配电室名称id
                break;
            case "2":
                map.put("titleType","水");
                map.put("tag","qf");
                map.put("titleUnit","(t)");
                map.put("categoryRelationTable", "water_project_category_relation");  // category
                map.put("meterRelationTable", "water_category_meter_relation");  // category_meter
                map.put("roomIdName", "water_pump_room_id");  // 配电室名称id
                break;
            case "3":
                map.put("titleType","气");
//                map.put("tag","gqf");
                map.put("tag","qf");
                map.put("titleUnit","(m)");
                map.put("categoryRelationTable", "water_project_category_relation");  // category
                map.put("meterRelationTable", "water_category_meter_relation");  // category_meter
                map.put("roomIdName", "water_pump_room_id");  // 配电室名称id
                /*
                map.put("categoryRelationTable", "gas_project_category_relation");  // category
                map.put("meterRelationTable", "gas_category_meter_relation");  // category_meter
                map.put("roomIdName", "gas_pressure_room_id");  // 配电室名称id*/
                break;
        }
        Date nowDate = DateUtil.parseStrToDate(String.valueOf(map.get("date")), DateUtil.DATE_FORMAT_YYYY_MM_DD);
        List<String> xAsis = new ArrayList<>();   //横坐标
        switch (String.valueOf(map.get("date_type"))) {
            case "day_data":
                map.put("nowTitlePre","当日");
                map.put("lastTitlePre","昨日");
                map.put("dateType", "power_per_hour");
                map.put("dataTable", "day_data_");
                map.put("nowTime", String.valueOf(map.get("date")));
                map.put("lastTime", DateUtil.parseDateToStr(DateUtil.addDate(nowDate, 0, 0, -1, 0, 0, 0, 0), DateUtil.DATE_FORMAT_YYYY_MM_DD));

                for (int i = 0; i <= 23; i++) {
                    xAsis.add(i + "h");
                }
                break;
            case "month_data":
                map.put("nowTitlePre","当月");
                map.put("lastTitlePre","上月");
                map.put("dateType", "power_per_day");
                map.put("dataTable", "month_data_");
                map.put("nowTime", DateUtil.parseStrToStr(String.valueOf(map.get("date")), DateUtil.DATE_FORMAT_YYYY_MM));
                map.put("lastTime", DateUtil.parseDateToStr(DateUtil.addDate(nowDate, 0, -1, 0, 0, 0, 0, 0), DateUtil.DATE_FORMAT_YYYY_MM));

                int nowDays = DateUtil.getDaysOfMonth(nowDate);
                int lastDays = DateUtil.getDaysOfMonth(DateUtil.addDate(nowDate, 0, -1, 0, 0, 0, 0, 0));
                int days = nowDays > lastDays ? nowDays : lastDays;
                for (int i = 1; i <= days; i++) {
                    xAsis.add(i + "d");
                }

                break;
            case "year_data":
                map.put("nowTitlePre","当年");
                map.put("lastTitlePre","去年");
                map.put("dateType", "power_per_month");
                map.put("dataTable", "year_data_");
                map.put("nowTime", DateUtil.getYear(nowDate));
                map.put("lastTime", DateUtil.getYear(DateUtil.addDate(nowDate, -1, 0, 0, 0, 0, 0, 0)));

                for (int i = 1; i <= 12; i++) {
                    xAsis.add(i + "m");
                }
                break;
        }
        map.put("dataTable",map.get("dataTable").toString()+map.get("tag").toString());

        CategoryMeterData treeUtil=new CategoryMeterData();
        //当前 年/月/日数据
        map.put("date", map.get("nowTime"));
        List<CategoryMeterData> nowCategoryData = elecAnalysisDao.getCategoryMeterData(map);
        List<CategoryMeterData> nowCategoryDataTree2 = elecAnalysisDao.getCategoryMeterDataTree(map);  //树
        List<CategoryMeterData> nowCategoryDataTree =treeUtil.getCategoryMeterDataByRecursion(Integer.parseInt(String.valueOf(map.get("category_id"))),nowCategoryDataTree2);


        //前一 年/月/日数据
        map.put("date", map.get("lastTime"));
        List<CategoryMeterData> lastCategoryData = elecAnalysisDao.getCategoryMeterData(map);
        List<CategoryMeterData> lastCategoryDataTree2 = elecAnalysisDao.getCategoryMeterDataTree(map);  //树
        List<CategoryMeterData> lastCategoryDataTree =treeUtil.getCategoryMeterDataByRecursion(Integer.parseInt(String.valueOf(map.get("category_id"))),lastCategoryDataTree2);

        //  CategoryId== Power
        Map<Integer, Double> lastCategoryMap = new HashMap<>();
        for (CategoryMeterData categoryMeterData:lastCategoryData){
            if (categoryMeterData.getPower()==0){  //数据为0  改category下边没有挂表
                CategoryMeterData category=treeUtil.getNodeById(categoryMeterData.getCategoryId(),lastCategoryDataTree);
                lastCategoryMap.put(categoryMeterData.getCategoryId(),category.getPower());
            }else{
                lastCategoryMap.put(categoryMeterData.getCategoryId(),categoryMeterData.getPower());
            }
        }
        //================ //====================
        //饼图
        Map<String, Object> pie = new HashMap<>();
        List<String> pieLegend = new ArrayList<>();
        List<Map<String, Object>> pieDataList = new ArrayList<>();

        //table
        Map<String, Object> table = new HashMap<>();
        List<Map<String, Object>> content = new ArrayList<>();
        //table 标题
        Map<String, Object> titleMap=new LinkedHashMap<>(2);
        List<Map> titleList = new ArrayList<>();
        titleMap.put("prop","title");
        titleMap.put("label","类型");
        titleList.add(titleMap);
        titleMap=new LinkedHashMap<>(2);
        titleMap.put("prop","now");
        titleMap.put("label",map.get("nowTitlePre")+"用"+map.get("titleType")+"量"+map.get("titleUnit"));
        titleList.add(titleMap);

        titleMap=new LinkedHashMap<>(2);
        titleMap.put("prop","ratetoall");
        titleMap.put("label","占比(%)");
        titleList.add(titleMap);

        titleMap=new LinkedHashMap<>(2);
        titleMap.put("prop","last");
        titleMap.put("label",map.get("lastTitlePre")+"用"+map.get("titleType")+"量"+map.get("titleUnit"));
        titleList.add(titleMap);

        titleMap=new LinkedHashMap<>(2);
        titleMap.put("prop","circular_rate");
        titleMap.put("label","环比(%)");
        titleList.add(titleMap);

        double nowPowerSum=0.0;
        for (int i = 0; i < nowCategoryData.size(); i++) {
            if (nowCategoryData.get(i).getPower()==0){  //数据为0  该category下边没有挂表
                CategoryMeterData category=treeUtil.getNodeById(nowCategoryData.get(i).getCategoryId(),nowCategoryDataTree);
                nowPowerSum+=category.getPower();
            }else{
                nowPowerSum+=nowCategoryData.get(i).getPower();
            }
        }


        double countNowPower=0;
        double countLastPower=0;
        double countCircularRate=0;
        //数据处理
        for (int i = 0; i < nowCategoryData.size(); i++) {
            CategoryMeterData categoryMeterData=nowCategoryData.get(i);

            double nowPower=0;
            if (categoryMeterData.getPower()==0){  //数据为0  该category下边没有挂表
                CategoryMeterData category=treeUtil.getNodeById(categoryMeterData.getCategoryId(),nowCategoryDataTree);
                nowPower=category.getPower();
            }else{
                nowPower=categoryMeterData.getPower();
            }

            double lastPower=lastCategoryMap.get(categoryMeterData.getCategoryId());
            //饼图数据
            Map<String, Object> pieData = new HashMap<>();
            pieData.put("name",categoryMeterData.getCategoryName());
            pieData.put("category_id",categoryMeterData.getCategoryId());
            if (treeUtil.getChildrenNodeById(categoryMeterData.getCategoryId(),nowCategoryDataTree2).size()==0){
                pieData.put("childLen",false);
            }else{
                pieData.put("childLen",true);
            }
            pieData.put("parent_category_id",categoryMeterData.getParentCategoryId());
            pieData.put("value",nowPower);

            pieLegend.add(categoryMeterData.getCategoryName());

            pieDataList.add(pieData);

            //table
            Map<String, Object> tableMap=new HashMap<>();
            tableMap.put("title",categoryMeterData.getCategoryName());
            tableMap.put("now",nowPower);
            countNowPower+=nowPower;
            tableMap.put("last",lastPower);
            countLastPower+=lastPower;
            tableMap.put("circular_rate",(double)Math.round(((nowPower-lastPower)/lastPower * 100) * 100) / 100d);
            tableMap.put("ratetoall",(double)Math.round((nowPower/nowPowerSum * 100) * 100) / 100);
            content.add(tableMap);
        }
        //总计
        Map<String, Object> countTableMap=new HashMap<>();
        countTableMap.put("title","总计");
        countTableMap.put("now",(double)Math.round(countNowPower * 100) / 100);
        countTableMap.put("last",(double)Math.round(countLastPower * 100) / 100);
        countTableMap.put("circular_rate",(double)Math.round(((countNowPower-countLastPower)/countLastPower * 100) * 100) / 100);
        countTableMap.put("ratetoall",100);
        content.add(countTableMap);

        pie.put("data",pieDataList);
        pie.put("legend",pieLegend);

        table.put("title",titleList);
        table.put("content",content);

        //================ //====================
        List<String> nowDataList = elecAnalysisDao.getDetailData(map);
        //包含空 证明有的category下边没挂表
        if (nowDataList.contains(null)||nowDataList.size()==0){
            List<String> nowDataList2 =new ArrayList<>();
                for (CategoryMeterData categoryMeterData:nowCategoryDataTree){
                    nowDataList2.addAll(categoryMeterData.getDetailList());
                }
            nowDataList=nowDataList2;
        }
        List<String> lastDataList = elecAnalysisDao.getDetailData(map);
        //包含空 证明有的category下边没挂表
        if (lastDataList.contains(null)||lastDataList.size()==0){
            List<String> lastDataList2 =new ArrayList<>();
            for (CategoryMeterData categoryMeterData:lastCategoryDataTree){
                lastDataList2.addAll(categoryMeterData.getDetailList());
            }
            lastDataList=lastDataList2;
        }


        //compare
        Map<String, Object> compare = new LinkedHashMap<>(2);
        Map<String, Object> compareNow = new HashMap<>();
        List<Double> nowData = new ArrayList<>();

        Map<String, Object> compareLast = new HashMap<>();
        List<Double> lastData = new ArrayList<>();

        if(!(nowDataList.size() > 0 && nowDataList.get(0) == null)){
            for (int i=0;i<xAsis.size();i++){
                double now=0.0;
                double last=0.0;
                for (int j=0;j<nowDataList.size();j++){
                    List<Double> nowList=JSONArray.parseArray(nowDataList.get(j),Double.class);
                    if(nowList.size() <= i) now+=0.0;
                    else now+=nowList.get(i);
                }

                for (int j=0;j<lastDataList.size();j++){
                    List<Double> lastList=JSONArray.parseArray(lastDataList.get(j),Double.class);
                    if(lastList.size() <= i) last+=0.0;
                    else last+=lastList.get(i);
                }
                nowData.add((double) Math.round(now * 100)/100);
                lastData.add((double) Math.round(last * 100)/100);
            }
        }

        compareNow.put("name",map.get("nowTitlePre")+"用"+map.get("titleType")+"量");
        compareNow.put("xAxis",xAsis);
        compareNow.put("data",nowData);

        compareLast.put("name",map.get("lastTitlePre")+"用"+map.get("titleType")+"量");
        compareLast.put("xAxis",xAsis);
        compareLast.put("data",lastData);

        compare.put("now",compareNow);
        compare.put("last",compareLast);

        //返回数据+数据条数
        Map<String, Object> result = new LinkedHashMap<>(3);
        result.put("pie",pie);  //饼图
        result.put("table",table);  //表格
        result.put("compare",compare);   //能耗同比
        return WebResult.success(result);
    }

    /**
     * 获取负载率（电-支路用能）
     * @param map
     * @return
     */
    @Override
    public WebResult getLoadRate(Map<String, Object> map) {
        double active_power=0.0;
        double reactive_power=0.0;
        if(map.get("date") == null||String.valueOf(map.get("date")).equals("")){
            map.put("date",DateUtil.parseDateToStr(new Date(),DateUtil.DATE_FORMAT_YYYY_MM_DD));
        }
        //当日 有功和 无功功率
        List<Map<String, String>> pAndQData=elecAnalysisDao.getPAndQData(map);
        for (Map<String, String> pAndQ:pAndQData){
            if (pAndQ.get("tag_name").equals("p")){
                double pval=0.0;
                List<Map> pList=JSONArray.parseArray(pAndQ.get("detail"),Map.class);
                for (Map p:pList){
                    pval+=Double.parseDouble(String.valueOf(p.get("val")));
                }
                active_power=(double)Math.round((pval/pList.size()) * 100) / 100;
            }

            if (pAndQ.get("tag_name").equals("q")){
                List<Map> qList=JSONArray.parseArray(pAndQ.get("detail"),Map.class);
                double qval=0.0;
                for (Map q:qList){
                    qval+=Double.parseDouble(String.valueOf(q.get("val")));
                }
                reactive_power=(double)Math.round((qval/qList.size()) * 100) / 100;
            }

        }

        List<String> xAsis = new ArrayList<>();   //横坐标
        for (int i = 0; i <= 23; i++) {
            xAsis.add(i + "h");
        }

        //当日数据
        Map<String, String> nowloadRateData=elecAnalysisDao.getLoadRateData(map);
        //昨日数据
        Date yesDate=DateUtil.addDate(DateUtil.parseStrToDate(String.valueOf(map.get("date")),DateUtil.DATE_FORMAT_YYYY_MM_DD),0,0,-1,0,0,0,0);
        map.put("date",DateUtil.parseDateToStr(yesDate,DateUtil.DATE_FORMAT_YYYY_MM_DD));
        Map<String, String> lastloadRateData=elecAnalysisDao.getLoadRateData(map);

        //当日负载率
        Map<String, Object> seriesMap=new HashMap<>();
        seriesMap.put("name","当日负载率");
        List<Map> nowList=new ArrayList<>();
        if (nowloadRateData!=null){
            nowList=JSONArray.parseArray(nowloadRateData.get("detail"),Map.class);
        }
        //当日负载率的值  添加默认空""
        List<String> seriesData = new ArrayList<>();
        for (int i = 0; i <= 23; i++){
            seriesData.add("");
        }

        for (Map loadRate:nowList){
            String log_time=String.valueOf(loadRate.get("log_time"));
            if (log_time.substring(14,16).equals("00")){
                int index=Integer.parseInt(log_time.substring(11,13));
                seriesData.set(index,loadRate.get("val").toString());
            }
        }
        seriesMap.put("data",seriesData);


        //昨日负载率
        Map<String, Object> seriesMap2=new HashMap<>();
        seriesMap2.put("name","昨日负载率");
        List<Map> lastList=new ArrayList<>();
        if (lastloadRateData!=null){
            lastList=JSONArray.parseArray(lastloadRateData.get("detail"),Map.class);
        }
        //昨日负载率的值  添加默认空""
        List<String> seriesData2 = new ArrayList<>();
        for (int i = 0; i <= 23; i++){
            seriesData2.add("");
        }
        for (Map loadRate:lastList){
            String log_time=String.valueOf(loadRate.get("log_time"));
            if (log_time.substring(14,16).equals("00")){
                int index=Integer.parseInt(log_time.substring(11,13));
                seriesData2.set(index,loadRate.get("val").toString());
            }
        }
        seriesMap2.put("data",seriesData2);

        //负载率数据
        List<Map<String, Object>> series = new ArrayList<>();
        series.add(seriesMap);  //当日
        series.add(seriesMap2);  //昨日

        List<String> legend = new ArrayList<>();
        legend.add("当日负载率");
        legend.add("昨日负载率");


        Map<String, String> loadRate=elecAnalysisDao.getLoadRate(map);
        //返回数据
        Map<String, Object> result = new LinkedHashMap<>(6);
        result.put("active_power",active_power);
        result.put("reactive_power",reactive_power);
        result.put("voltage_level",loadRate.get("voltage_level"));
        result.put("xAsis",xAsis);
        result.put("series",series);
        result.put("legend",legend);
        return WebResult.success(result);
    }
}
