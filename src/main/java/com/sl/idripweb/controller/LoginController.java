package com.sl.idripweb.controller;


import com.sl.common.swagger.annos.ApiJsonObject;
import com.sl.common.swagger.annos.ApiJsonProperty;
import com.sl.common.utils.RedisUtil;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "login")
@Api(value = "login", description = "用户登录、退出接口、修改密码接口")
public class LoginController {

    @Autowired
    UserService userService;

    /**
     * 登录
     * @access public
     * @param uname 用户名
     * @param pwd 密码(前端已经md5)
     * @return array 返回类型
     */
    @ApiOperation(value = "login", notes = "用户登录接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uname", value = "用户名", example = "test"),
            @ApiImplicitParam(name = "pwd", value = "密码", example = "dec13e5dd838c1257eaca635ad5f308b")
    }
    )
    @RequestMapping(value = "login",method = RequestMethod.POST)
    public WebResult login(HttpServletRequest request, String uname, String pwd){
        return userService.login(request, uname, pwd);
    }

    /**
     * 注销登录
     * @param request
     * @return
     */
    @ApiOperation(value = "logout", notes = "注销登录", httpMethod = "GET")
    @RequestMapping(value = "logout",method = RequestMethod.GET)
    public WebResult logout(HttpServletRequest request){
        return userService.logout(request);
    }


    /**
     * 显示登录页信息
     * @param hostname
     * @return
     */
    @ApiOperation(value = "loginpage", notes = "显示登录页信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hostname", value = "主机名称")
    }
    )
    @RequestMapping(value = "loginpage",method = RequestMethod.POST)
    public WebResult loginpage(String hostname){
        return userService.loginpage(hostname);
    }

    /**
     * 修改用户密码
     * @param
     * @return
     */
    @ApiOperation(value = "update_pwd", notes = "修改密码", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = String.class,key = "uname", example = "test1", description = "用户名"),
            @ApiJsonProperty(type = String.class,key = "pwd", example = "d0e1e2dc9473cdb301f7e97de3bda3cd", description = "新密码"),
            @ApiJsonProperty(type = String.class,key = "old_pwd", example = "d0e1e2dc9473cdb301f7e97de3bda3cd1", description = "旧密码")
    })
    @RequestMapping(value = "update_pwd",method = RequestMethod.POST)
    public WebResult loginpage(HttpServletRequest request, @RequestAttribute Map<String,Object> map){
        return userService.updatePwd(request, map);
    }

}
