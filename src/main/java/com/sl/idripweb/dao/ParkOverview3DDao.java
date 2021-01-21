package com.sl.idripweb.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ParkOverview3DDao {
     double transformerroomEnergyData(Map<String, Object> map);
     List<Map<String, Object>> getTemperatureController(Map<String, Object> map);

}
