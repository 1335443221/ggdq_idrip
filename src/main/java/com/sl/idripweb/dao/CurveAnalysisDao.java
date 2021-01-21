package com.sl.idripweb.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface CurveAnalysisDao {

    /**
     * 获取设备列表(曲线分析)
     * @param
     * @return
     */
    ArrayList<Map<String, Object>> getRoomDevice(Map<String, Object> map);

    /**
     * 获取变压器低压进线表(曲线分析)
     * @param
     * @return
     */
    ArrayList<Map<String, Object>> getLowIncomingLine(Map<String, Object> map);

    /**
     * 按表查找对应数据
     * @param map
     * @return
     */
    ArrayList<Map<String, Object>> getHistoryData(Map<String, Object> map);

    /**
     * 获取标线告警配置数据
     * @param deviceId
     * @return
     */
    ArrayList<Map<String, Object>> getThreshold(@Param("deviceId") String deviceId);
}
