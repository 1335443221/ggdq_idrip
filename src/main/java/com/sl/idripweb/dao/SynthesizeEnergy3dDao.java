package com.sl.idripweb.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface SynthesizeEnergy3dDao {

    //获取需要展示的地图上厂区
    Map<String, Object> getDisplayFactorys(Map<String, Object> map);

    //获取今年电量年统计
    List<Map<String, Object>> getElecMeterYearData(Map<String, Object> map);

    //获取所有子表电表标签
    List<String> getElecCategoryMeterList(Map<String, Object> map);

    //获取所有子表水表标签
    List<String> getWaterCategoryMeterList(Map<String, Object> map);

    //获取今年水量年统计
    List<Map<String, Object>> getWaterMeterYearData(Map<String, Object> map);

    //获取所有子表气表标签
    List<String> getGasCategoryMeterList(Map<String, Object> map);

    //获取今年气量年统计
    List<Map<String, Object>> getGasMeterYearData(Map<String, Object> map);

    //各区域用水量统计
    List<Map<String, Object>> getAreaWaterYearData(Map<String, Object> map);

    //用户用电排名
    List<Map<String, Object>> getElecRanking(Map<String, Object> map);

    //用户用水排名
    List<Map<String, Object>> getWaterRanking(Map<String, Object> map);

    //用户用气排名
    List<Map<String, Object>> getGasRanking(Map<String, Object> map);


}
