package com.sl.idripweb.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessageManageDao {

    List<Map<String,Object>> getMessageInfo(Map<String, Object> map);
    int getMessageInfoCount(Map<String, Object> map);
    List<Map<String, Object>> getMessageErrorcodeInfo(Map<String, Object> map);

    List<Map<String,Object>> getMessageCount(Map<String, Object> map);
    int getMessageCountCount(Map<String, Object> map);
    List<Map<String,Object>> getAccountInfo(Map<String, Object> map);

    int save(Map<String, Object> map);
    int update(Map<String, Object> map);
    int delete(Map<String, Object> map);
    List<Map<String,Object>> query(Map<String, Object> map);
    int queryCount(Map<String, Object> map);
    List<Map<String,Object>> getRoles(Map<String, Object> map);
    List<Map<String,Object>> getUserByRoleId(Map<String, Object> map);

}
