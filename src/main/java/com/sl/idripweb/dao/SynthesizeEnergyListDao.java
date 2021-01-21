package com.sl.idripweb.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SynthesizeEnergyListDao {

    //计算配电室数量
    int getElecCategoryCount(Map<String, Object> map);

    //计算水泵房数量
    int getWaterCategoryCount(Map<String, Object> map);

    //计算燃气调压站数量
    int getGasCategoryCount(Map<String, Object> map);

    //计算电表数量
    int getElecMeterCount(Map<String, Object> map);

    //计算水表数量
    int getWaterMeterCount(Map<String, Object> map);

    //计算燃气表数量
    int getGasMeterCount(Map<String, Object> map);

    //计算电-年数据
    Double getElecYearData(Map<String, Object> map);

    //计算电-月数据
    Double getElecMonthData(Map<String, Object> map);

    //计算电-天数据
    List<String> getElecDayData(Map<String, Object> map);

    //计算水-年数据
    Double getWaterYearData(Map<String, Object> map);

    //计算水-月数据
    Double getWaterMonthData(Map<String, Object> map);

    //计算水-天数据
    List<String> getWaterDayData(Map<String, Object> map);

    //计算气-年数据
    Double getGasYearData(Map<String, Object> map);

    //计算气-月数据
    Double getGasMonthData(Map<String, Object> map);

    //计算气-天数据
    List<String> getGasDayData(Map<String, Object> map);


}
