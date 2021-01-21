package com.sl.idripweb.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface TgDao {

    /**
     * 获取所有通讯机
     * @param
     * @return
     */
    ArrayList<Map<String, Object>> getTgList(@Param("pid") String pid);

}
