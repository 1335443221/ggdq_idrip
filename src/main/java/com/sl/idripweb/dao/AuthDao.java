package com.sl.idripweb.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface AuthDao {
    /**
     * 获取菜单权限
     * @param
     * @return
     */
    ArrayList<Map<String, Object>> getMenuList(@Param("pid") String pid, @Param("uid") String uid);

    /**
     * 获取菜单权限2
     * @param
     * @return
     */
    ArrayList<Map<String, Object>> getMenuList2(@Param("uid") String uid);

    /**
     * 获取视图权限
     * @param
     * @return
     */
    ArrayList<String> getViewList(@Param("uid") String uid);

    /**
     * 获取数据权限
     * @param
     * @return
     */
    ArrayList<Map<String, Object>> getDataList(@Param("uid") String uid);

    /**
     * 获取全部需要验证的view
     * @param
     * @return
     */
    ArrayList<String> getNeedAuthView();

}
