package com.sl.idripweb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sl.common.utils.DateUtil;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.config.MySession;
import com.sl.idripweb.dao.SynthesizeEnergy3dDao;
import com.sl.idripweb.service.SynthesizeEnergy3dService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class SynthesizeEnergy3dServiceImpl implements SynthesizeEnergy3dService {

    @Autowired
    SynthesizeEnergy3dDao energy3dDao;
    @Autowired
    MySession mySession;
    @Autowired
    DateUtil dateUtil;

    //能耗分析==>综合能耗地图首页
    @Override
    public WebResult index(HttpServletRequest request) {

        Map<String, Object> result = new HashMap<>();

        String pid = mySession.getPid(request);
        Map<String, Object> projectInfo = mySession.getProjectInfo(request);
        String projectCodeName = String.valueOf(projectInfo.get("code_name"));
        String projectSafeRunDay = String.valueOf(projectInfo.get("safe_day"));
        Map<String, Object> params = new HashMap<>();
        params.put("project_id", pid);
        params.put("key_name", "display_factory_" + projectCodeName);
        params.put("date", dateUtil.getTodayYear());
        Map<String, Object> displayFactorys = energy3dDao.getDisplayFactorys(params);
        List<Map> displayFactorysDetail = JSONArray.parseArray(displayFactorys.get("description").toString(), Map.class);

        //获取session中的厂区信息
        List<Map> factoryInfo = mySession.getFactoryInfo(request);
        List<Map> feFactoryList = new ArrayList<>();
        for(Map each : factoryInfo){
            String factoryName = String.valueOf(each.get("factory_name"));
            String longitude = String.valueOf(each.get("longitude"));
            String latitude = String.valueOf(each.get("latitude"));
            String id = String.valueOf(each.get("id"));
            Map<String, Object> eachMap = new HashMap<>();
            eachMap.put("name", factoryName);
            eachMap.put("point", (longitude + "," + latitude).split(","));
            eachMap.put("id", id);
            eachMap.put("access", 1);
            feFactoryList.add(eachMap);
        }
        feFactoryList.addAll(displayFactorysDetail);

        //计算综合能耗地图电能趋势
            //获取所有子表电表标签elec_meter
        List<String> elecCategoryMeterList = energy3dDao.getElecCategoryMeterList(params);
            //查询电表对应的电量年统计
        List<Map<String, Object>> elecMeterYearData = new ArrayList<>();
        if(elecCategoryMeterList.size() > 0){
            params.put("meter_list", elecCategoryMeterList);
            elecMeterYearData = energy3dDao.getElecMeterYearData(params);
        }
            //计算每个月的用电量
        double[] elecSum = new double[12];
        int[] monthList = new int[12];
        double sunElec = 0.0;
        for(Map<String, Object> each : elecMeterYearData){
            List<Double> powerPerMonth = JSONArray.parseArray(each.get("power_per_month").toString(), Double.class);
            for(int i=0;i<powerPerMonth.size();i++){
                elecSum[i] = Math.round((powerPerMonth.get(i) + elecSum[i]) * 100)/100.0;
                sunElec += powerPerMonth.get(i);
                monthList[i] = i + 1;
            }
        }
        //封装电能趋势结果
        Map<String, Object> elecChart = new HashMap<>();
        elecChart.put("data_unit", "kWh");
        elecChart.put("time_unit", "m");
        elecChart.put("title", monthList);
        elecChart.put("value", elecSum);


        //用户用电排名
        List<Map<String, Object>> elecData = energy3dDao.getElecRanking(params);
        //分组处理，将同一区域的数据放到一个组
        handleRanking(elecData, "kWh");


        //计算综合能耗地图水能趋势
            //获取所有子表水表标签elec_meter
        List<String> waterCategoryMeterList = energy3dDao.getWaterCategoryMeterList(params);
            //查询水表对应的水量年统计
        List<Map<String, Object>> waterMeterYearData = new ArrayList<>();
        if(waterCategoryMeterList.size() > 0){
            params.put("meter_list", waterCategoryMeterList);
            waterMeterYearData = energy3dDao.getWaterMeterYearData(params);
        }
            //计算每个月水用量
        double[] waterSum = new double[12];
        double sumWater = 0.0;
        for(Map<String, Object> each : waterMeterYearData){
            List<Double> powerPerMonth = JSONArray.parseArray(each.get("power_per_month").toString(), Double.class);
            for(int i=0;i<powerPerMonth.size();i++){
                waterSum[i] = Math.round((powerPerMonth.get(i) + waterSum[i]) * 100)/100.0;
                sumWater += powerPerMonth.get(i);
            }
        }
        //封装水能趋势结果
        Map<String, Object> waterChart = new HashMap<>();
        waterChart.put("data_unit", "t");
        waterChart.put("time_unit", "m");
        waterChart.put("title", monthList);
        waterChart.put("value", waterSum);

        //用户用水排名
        List<Map<String, Object>> waterData = energy3dDao.getWaterRanking(params);
        //分组处理，将同一区域的数据放到一个组
        handleRanking(waterData, "t");


        //各区域用水量统计
        List<Map<String, Object>> areaWaterYearData = energy3dDao.getAreaWaterYearData(params);
        Map<String, Object> waterAreaChart = new HashMap<>();
        waterAreaChart.put("data_unit", "m³");
        List<String> areaList = new ArrayList<>();
        List<String> areaDataList = new ArrayList<>();
        for(Map<String, Object> each : areaWaterYearData){
            areaList.add(String.valueOf(each.get("factory_name")));
            areaDataList.add(String.valueOf(each.get("power")));
        }
        waterAreaChart.put("title", areaList);
        waterAreaChart.put("value", areaDataList);

        //计算综合能耗地图气能趋势
            //获取所有子表气表标签elec_meter
        List<String> gasCategoryMeterList = energy3dDao.getGasCategoryMeterList(params);
            //查询气表对应的气量年统计
        List<Map<String, Object>> gasMeterYearData = new ArrayList<>();
        if(gasCategoryMeterList.size() > 0){
            params.put("meter_list", gasCategoryMeterList);
            gasMeterYearData = energy3dDao.getGasMeterYearData(params);
        }

        //计算每个月用量
        double[] gasSum = new double[12];
        double sumGas = 0.0;
        for(Map<String, Object> each : gasMeterYearData){
            List<Double> powerPerMonth = JSONArray.parseArray(each.get("power_per_month").toString(), Double.class);
            for(int i=0;i<powerPerMonth.size();i++){
                gasSum[i] = Math.round((powerPerMonth.get(i) + gasSum[i]) * 100)/100.0;
                sumGas += powerPerMonth.get(i);
            }
        }
        //封装气能趋势结果
        Map<String, Object> gasChart = new HashMap<>();
        gasChart.put("data_unit", "m³");
        gasChart.put("time_unit", "m");
        gasChart.put("title", monthList);
        gasChart.put("value", gasSum);

        //用户用气排名
        List<Map<String, Object>> gasData = energy3dDao.getGasRanking(params);
        //分组处理，将同一区域的数据放到一个组
        handleRanking(gasData, "m³");

        //计算总能耗
        double allTypeEnergySum = Math.round((Math.round(sunElec*12.29)/100.00 + Math.round(sumWater*24.29)/100.00 + Math.round(sumGas*129.97)/100.00)*100)/100.00;

        List<String> elecDataTitle = Arrays.asList("排序,区域,配电室,用电量（kWh）,总用电（kWh）".split(","));
        List<String> waterDataTitle = Arrays.asList("排序,区域,水泵房,用水量（t）,总用水（t）".split(","));
        List<String> gasDataTitle = Arrays.asList("排序,区域,调压站,用气量（m³）,总用气（m³）".split(","));
        result.put("project_safe_run_day", projectSafeRunDay);
        result.put("elecDataTitle", elecDataTitle);
        result.put("waterDataTitle", waterDataTitle);
        result.put("gasDataTitle", gasDataTitle);
        result.put("project_date", dateUtil.parseTimestampToStr(Long.parseLong(String.valueOf(projectInfo.get("date_time"))), "yyyy-MM-dd HH:mm:ss"));
        result.put("fe_factory_list", feFactoryList);
        result.put("elec_chart", elecChart);
        result.put("water_chart", waterChart);
        result.put("gas_chart", gasChart);
        result.put("water_area_chart", waterAreaChart);
        result.put("all_type_energy_sum", allTypeEnergySum);
        result.put("elecData", elecData);
        result.put("waterData", waterData);
        result.put("gasData", gasData);
        return WebResult.success(result);
    }

    //处理排名结果集
    private void handleRanking(List<Map<String, Object>> elecRanking, String unit) {
        Map<String, Double> elecRankingMap = new HashMap<>();
        for(Map<String, Object> each : elecRanking){
            String factoryId = String.valueOf(each.get("factory_id"));
            double power = Double.parseDouble(String.valueOf(each.get("used")));
            double existPower = 0.0;
            if(elecRankingMap.get(factoryId) != null)
                existPower = elecRankingMap.get(factoryId);
            elecRankingMap.put(factoryId, power + existPower);
        }
        //排序计算index
        List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(elecRankingMap.entrySet());

        Collections.sort(list,new Comparator<Map.Entry<String,Double>>() {
            //升序排序
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        //给用户用电排名赋值index和totalUsed
        for(Map<String, Object> each : elecRanking){
            String factoryId = String.valueOf(each.get("factory_id"));
            for(int i=0;i<list.size();i++){
                if(factoryId.equals(list.get(i).getKey())){
                    each.put("index", i+1);
                    each.put("totalUsed", list.get(i).getValue());
                    each.put("unit", unit);
                    each.remove("factory_id");
                }
            }
        }
    }
}
