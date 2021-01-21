package com.sl.idripweb.dao;

import com.sl.idripweb.entity.elecAnalysis.CategoryMeterData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ElecAnalysisDao {

    //获取 水电气 历史信息
    List<String> getDetailData(Map<String, Object> map);

    //获取 水电气 分类列表
    List<CategoryMeterData> getCategoryMeterData(Map<String, Object> map);

    List<CategoryMeterData> getCategoryMeterDataTree(Map<String, Object> map);


    Map<String, String> getLoadRateData(Map<String, Object> map);
    List<Map<String, String>> getPAndQData(Map<String, Object> map);
    Map<String, String> getLoadRate(Map<String, Object> map);
}
