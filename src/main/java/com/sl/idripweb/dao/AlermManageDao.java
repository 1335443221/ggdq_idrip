package com.sl.idripweb.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface AlermManageDao{

    ArrayList<Map<String,Object>> getAlermData(Map<String,Object> map);
    int getAlermDataCount(Map<String, Object> map);


    ArrayList<Map<String,Object>> getAlermCategoryByPeoject(Map<String, Object> map);
    ArrayList<Map<String,Object>> getAlermCategoryN3();
    ArrayList<Map<String, Object>> getAllBusinessName(Map<String, Object> map);
    ArrayList<Map<String, Object>> getFactoryByProjectid(Map<String, Object> map);
    Map<String, Object> queryDealDetail(@Param("lid") String lid);

    int saveDeal(Map<String, Object> map);
    int updateDeal(Map<String, Object> map);
    Map<String, Object> selectAlermById(Map<String, Object> map);

    int  insertAllHistory(List<Map> list);
    int  deleteLog(@Param("lid") String lid);


    int updateIsRegister(@Param("lid") String lid);
    int updateIsRegisterHistory(@Param("lid") String lid);
    int saveRepairMsg(Map<String, Object> map);
    int updateRepairMsg(Map<String, Object> map);
    ArrayList<Map<String,Object>> getPopupData(Map<String, Object> map);
    int getPopupDataCount(Map<String, Object> map);

    int confirm(Map<String, Object> map);


    ArrayList<Map> printAlerm(Map<String, Object> map);

    ArrayList<Map<String, Object>> getAlermCateGoryByid(Map<String, Object> map);
    ArrayList<Map<String, Object>> getFactoryByid(Map<String, Object> map);
}
