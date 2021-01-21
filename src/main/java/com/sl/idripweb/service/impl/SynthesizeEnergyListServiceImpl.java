package com.sl.idripweb.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.sl.common.utils.DateUtil;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.config.MySession;
import com.sl.idripweb.dao.SynthesizeEnergy3dDao;
import com.sl.idripweb.dao.SynthesizeEnergyListDao;
import com.sl.idripweb.service.SynthesizeEnergyListService;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class SynthesizeEnergyListServiceImpl implements SynthesizeEnergyListService {

    @Autowired
    MySession mySession;
    @Autowired
    SynthesizeEnergyListDao energyListDao;
    @Autowired
    SynthesizeEnergy3dDao energy3dDao;
    @Autowired
    DateUtil dateUtil;


    //能耗分析==>综合能耗列表
    @Override
    public WebResult index(HttpServletRequest request, Map<String,Object> map) {
        String factoryId = String.valueOf(map.get("factory_id"));
        String dateTo = String.valueOf(map.get("date_to"));
        if(factoryId == null || StringUtils.isEmpty(factoryId))
            factoryId = mySession.getFactoryId(request);
        if(dateTo == null || StringUtils.isEmpty(dateTo))
            dateTo = dateUtil.getYestoday();
        map.put("factory_id", factoryId);
        String todayDate = dateTo;
        String yesterdayDate = dateUtil.getBeforeDayOfTime(dateTo, "yyyy-MM-dd");
        map.put("date_to_month", DateUtil.parseStrToStr(dateTo, "yyyy-MM"));
        map.put("date_to_year", DateUtil.parseStrToStr(dateTo, "yyyy"));
        Map factory = mySession.getFactoryById(request, factoryId);

        //获取项目厂区信息
        Map<String, Object> projectFactorys = mySession.getProjectFactorys(request);

        //获取配电室数量
        int elecCategoryCount = energyListDao.getElecCategoryCount(map);

        //计算水泵房数量
        int waterCategoryCount = energyListDao.getWaterCategoryCount(map);

        //计算燃气调压站数量
        int gasCategoryCount = energyListDao.getGasCategoryCount(map);

        //计算电表数量
        int elecMeterCount = energyListDao.getElecMeterCount(map);

        //计算水表数量
        int waterMeterCount = energyListDao.getWaterMeterCount(map);

        //计算燃气表数量
        int gasMeterCount = energyListDao.getGasMeterCount(map);

        //分类用能占比结果集
        Map<String, Map<String, Object>> allTypeEnergyYearSum = new LinkedHashMap<>();
        allTypeEnergyYearSum.put("elec", new HashMap<>());
        allTypeEnergyYearSum.put("gas", new HashMap<>());
        allTypeEnergyYearSum.put("water", new HashMap<>());
        allTypeEnergyYearSum.get("elec").put("name", "电");
        allTypeEnergyYearSum.get("water").put("name", "水");
        allTypeEnergyYearSum.get("gas").put("name", "气");
        String elecYearData = "0.00";
        String elecMonthData = "0.00";
        String waterYearData = "0.00";
        String waterMonthData = "0.00";
        String gasYearData = "0.00";
        String gasMonthData = "0.00";
        List<Double> elecDayDataYesterday = new ArrayList<>();
        List<Double> elecDayDataToday = new ArrayList<>();
        List<Double> waterDayDataYesterday = new ArrayList<>();
        List<Double> waterDayDataToday = new ArrayList<>();
        List<Double> gasDayDataYesterday = new ArrayList<>();
        List<Double> gasDayDataToday = new ArrayList<>();
        //分类用能概况结果集
        Map<String, Object> allTypeEnergySumList = new LinkedHashMap<>();

        allTypeEnergySumList.put("title", "分类,当月能耗,当年能耗,单位".split(","));
        //电计算
            //获取所有子表电表标签
        List<String> elecCategoryMeterList = energy3dDao.getElecCategoryMeterList(map);
        if(elecCategoryMeterList.size() > 0){
            //设置查询参数meter_ids
            map.put("meter_ids", elecCategoryMeterList);
            //获取年电量数据
            elecYearData = String.format("%.2f", energyListDao.getElecYearData(map));
            //获取月电量数据
            elecMonthData = String.format("%.2f", energyListDao.getElecMonthData(map));
            //电封装
            allTypeEnergySumList.put("elec", ("电,"+elecMonthData+","+elecYearData+","+"kWh").split(","));
            //电分类用能结果计算
            allTypeEnergyYearSum.get("elec").put("value", Math.round(Double.parseDouble(elecYearData)*12.29)/100.00);
            //获取天电量数据--昨天
            map.put("date", yesterdayDate);
            elecDayDataYesterday = handleDataAdd(energyListDao.getElecDayData(map), yesterdayDate);
            //获取天电量数据--今天
            map.put("date", todayDate);
            elecDayDataToday = handleDataAdd(energyListDao.getElecDayData(map), todayDate);
        }else{
            //电封装
            allTypeEnergySumList.put("elec", ("电,-,-,kWh").split(","));
            //电分类用能结果计算
            allTypeEnergyYearSum.get("elec").put("value", 0);
        }


        //水计算
            //获取所有子表水表标签
        List<String> waterCategoryMeterList = energy3dDao.getWaterCategoryMeterList(map);
        if(waterCategoryMeterList.size() > 0){
            //设置查询参数meter_ids
            map.put("meter_ids", waterCategoryMeterList);
            //获取年水量数据
            waterYearData = String.format("%.2f", energyListDao.getWaterYearData(map));
            //获取月水量数据
            waterMonthData = String.format("%.2f", energyListDao.getWaterMonthData(map));
            //水封装
            allTypeEnergySumList.put("water", ("水,"+waterMonthData+","+waterYearData+","+"t").split(","));
            //水分类用能结果计算
            allTypeEnergyYearSum.get("water").put("value", Math.round(Double.parseDouble(waterYearData)*24.29)/100.00);
            //获取天水量数据--昨天
            map.put("date", yesterdayDate);
            waterDayDataYesterday = handleDataAdd(energyListDao.getWaterDayData(map), yesterdayDate);
            //获取天电量数据--今天
            map.put("date", todayDate);
            waterDayDataToday = handleDataAdd(energyListDao.getWaterDayData(map), todayDate);
        }else{
            //水封装
            allTypeEnergySumList.put("water", ("水,-,-,t").split(","));
            //水分类用能结果计算
            allTypeEnergyYearSum.get("water").put("value", 0);
        }


        //气计算
            //获取所有气表气表标签
        List<String> gasCategoryMeterList = energy3dDao.getGasCategoryMeterList(map);
        if(gasCategoryMeterList.size() > 0){
            //设置查询参数meter_ids
            map.put("meter_ids", gasCategoryMeterList);
            //获取年气量数据
            gasYearData = String.format("%.2f", energyListDao.getGasYearData(map));
            //获取月气量数据
            gasMonthData = String.format("%.2f", energyListDao.getGasMonthData(map));
            //气封装
            allTypeEnergySumList.put("gas", ("气,"+gasMonthData+","+gasYearData+","+"m³").split(","));
            //气分类用能结果计算
            allTypeEnergyYearSum.get("gas").put("value", Math.round(Double.parseDouble(gasYearData)*129.97)/100.00);
            //获取天水量数据--昨天
            map.put("date", yesterdayDate);
            gasDayDataYesterday = handleDataAdd(energyListDao.getWaterDayData(map), yesterdayDate);
            //获取天电量数据--今天
            map.put("date", todayDate);
            gasDayDataToday = handleDataAdd(energyListDao.getGasDayData(map), todayDate);
        }else{
            //气封装
            allTypeEnergySumList.put("gas", ("气,-,-,m³").split(","));
            //气分类用能结果计算
            allTypeEnergyYearSum.get("gas").put("value", 0);
        }


        //综合用能计算
        double monthSum = Math.round((Double.parseDouble(gasMonthData)*129.97) + (Double.parseDouble(waterMonthData)*24.29) + (Double.parseDouble(elecMonthData)*12.29))/100.00;
        double yearSum = Math.round((Double.parseDouble(gasYearData)*129.97) + (Double.parseDouble(waterYearData)*24.29) + (Double.parseDouble(elecYearData)*12.29))/100.00;
        allTypeEnergySumList.put("sum", ("综合,"+monthSum+","+yearSum+","+"千克标准煤").split(","));


        //天能耗曲线结果集
        Map<String, Object> brokenLine = new HashMap<>();
            //电每天数据
        Map<String, Object> dayElec = new HashMap<>();
        dayElec.put("before", elecDayDataYesterday);
        dayElec.put("current", elecDayDataToday);
        dayElec.put("operation_data_day_data_unit", "kWh");
            //水每天数据
        Map<String, Object> dayWater = new HashMap<>();
        dayWater.put("before", waterDayDataYesterday);
        dayWater.put("current", waterDayDataToday);
        dayWater.put("operation_data_day_data_unit", "t");
            //气每天数据
        Map<String, Object> dayGas = new HashMap<>();
        dayGas.put("before", gasDayDataYesterday);
        dayGas.put("current", gasDayDataToday);
        dayGas.put("operation_data_day_data_unit", "m³");
        brokenLine.put("title_time_unit", "h");
        brokenLine.put("elec", dayElec);
        brokenLine.put("water", dayWater);
        brokenLine.put("gas", dayGas);

        //能耗曲线title
        List<Integer> title = new ArrayList<>();

        for(int i=0;i<24;i++){
            title.add(i);
        }
        brokenLine.put("title", title);

        //要返回的结果集
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("build_num", factory.get("build_num"));
        resultMap.put("build_acreage", factory.get("build_acreage"));
        resultMap.put("elec_transformerroom", elecCategoryCount);
        resultMap.put("water_transformerroom", waterCategoryCount);
        resultMap.put("gas_transformerroom", gasCategoryCount);
        resultMap.put("elec_meter_num", elecMeterCount);
        resultMap.put("water_meter_num", waterMeterCount);
        resultMap.put("gas_meter_num", gasMeterCount);
        resultMap.put("project_factorys", projectFactorys);
        resultMap.put("selected_area_category_id", Integer.parseInt(factoryId));
        resultMap.put("selected_area_category_name", factory.get("factory_name"));
        resultMap.put("all_type_energy_sum_list", allTypeEnergySumList);
        resultMap.put("all_type_energy_year_sum", allTypeEnergyYearSum);
        resultMap.put("broken_line", brokenLine);

        return WebResult.success(resultMap);
    }

    //处理多个表每天的消耗相加
    private List<Double> handleDataAdd(List<String> dayData, String date) {
        int count = 24;
        if(DateUtil.isNow(date)){
            count = dateUtil.getHour(new Date());
        }
        List<Double> dayResult = new ArrayList<>(Collections.nCopies(count, 0.0));
        int finalCount = count;
        dayData.forEach(each -> {
            List<Double> doubles = JSONArray.parseArray(each, Double.class);
            for(int i = 0; i< finalCount; i++){
                dayResult.set(i, Math.round((dayResult.get(i) + doubles.get(i))*100)/100.0);
            }
        });
        return dayResult;
    }
}
