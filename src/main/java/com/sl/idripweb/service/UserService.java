package com.sl.idripweb.service;

import com.sl.common.utils.WebResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface UserService {

    /**
     * 用户登录
     * @param
     * @return
     */
    public WebResult login(HttpServletRequest request, String uname, String pwd);

    /**
     * 退出登录
     * @param request
     * @return
     */
    public WebResult logout(HttpServletRequest request);

    /**
     * 显示登录页信息
     * @param hostname
     * @return
     */
    public WebResult loginpage(String hostname);

    /**
     * 修改用户密码
     * @param
     * @return
     */
    public WebResult updatePwd(HttpServletRequest request, Map<String,Object> map);
}
