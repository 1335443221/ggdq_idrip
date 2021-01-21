package com.sl.idripweb.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ElecReportDao {

    List<Map<String, Object>> getCategoryList(Map<String, Object> map);
    List<Map<String, Object>> getEpData(Map<String, Object> map);

}
