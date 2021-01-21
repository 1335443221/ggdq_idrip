package com.sl.idripweb.dao;

import com.sl.common.entity.params.GetMeterParams;
import com.sl.idripweb.entity.alerm.CategoryRelationTree;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PowerDao {
    List<Map<String,Object>> getSelectLine(Map<String, Object> map);

    //获取配电室  根据厂区
    List<Map<String, Object>> getTransformerroomByPid(Map<String, Object> map);



}
