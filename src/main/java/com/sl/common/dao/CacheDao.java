package com.sl.common.dao;

import com.sl.common.entity.params.GetMeterParams;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CacheDao {
    //获取电表
    List<Map<String, Object>> getElecMeter(GetMeterParams getMeterParams);

    //获取电表标签
   List<Map<String, Object>> getElecTag();

    //电路图排序
    Map<String, Object> getSortData(GetMeterParams map);
}
