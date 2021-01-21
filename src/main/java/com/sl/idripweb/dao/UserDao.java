package com.sl.idripweb.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserDao {
    /**
     * 根据uname取用户数据
     * @access public
     * @param  uname 用户名
     * @return Map 用户信息
     */
    Map<String, Object> getUserInfo(@Param("uname") String uname);

    /**
     * 根据uid取用户项目信息
     * @access public
     * @param uid 用户ID
     * @return array 项目信息
     */
    Map<String, Object> getUserProjectInfo(@Param("uid") String uid);

    /**
     * 获取有权限的厂区
     * @access public
     * @param uid 用户ID
     * @return array 项目信息
     */
    ArrayList<Map<String, Object>> getAuthFactorys(@Param("pid") String pid, @Param("uid") String uid);

    /**
     * 添加用户登录日志
     * @param map
     * @return
     */
    int addUserLog(Map<String, Object> map);

    /**
     * 更新url_tips表
     * @param pid
     * @param lastTime
     * @return
     */
    int updateUrlTips(@Param("pid") String pid, @Param("last_time") String lastTime);

    /**
     * 根据pid取对应登录页信息
     * @access public
     * @param hostname 主机名称
     * @return
     */
    Map<String, Object> getUserLoginPage(@Param("hostname") String hostname);

    /**
     * 修改用户密码
     * @access public
     * @param map
     * @return
     */
    int updatePwd(Map<String, Object> map);

    /**
     * 根据厂区获取配电室/水泵房/调压站列表
     * @access public
     * @param map
     * @return
     */
    List<Map<String, Object>> getRoomList(Map<String, Object> map);


}
