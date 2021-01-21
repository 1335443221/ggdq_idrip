package com.sl.idripweb.service.impl;

import com.alibaba.fastjson.JSON;
import com.sl.common.utils.DateUtil;
import com.sl.common.utils.RedisUtil;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.dao.AuthDao;
import com.sl.idripweb.dao.TgDao;
import com.sl.idripweb.dao.UserDao;
import com.sl.idripweb.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AuthDao authDao;
    @Autowired
    private TgDao tgDao;

    @Override
    @Transactional
    public WebResult login(HttpServletRequest request, String uname, String pwd) {
        uname = StringUtils.trim(uname);
        if(StringUtils.isEmpty(uname) || uname.length() < 1 || uname.length() > 12){
            return WebResult.error(301);
        }
        //获取用户基础信息
        Map<String, Object> userInfo = userDao.getUserInfo(uname);
        if(userInfo == null || !pwd.equals(userInfo.get("pwd"))){
            return WebResult.error(601);
        }

        String uid = String.valueOf(userInfo.get("uid"));

        //获取用户项目信息
        Map<String, Object> projectInfo = userDao.getUserProjectInfo(uid);
        if(projectInfo == null || projectInfo.size() == 0){
            return WebResult.error(602);
        }

        String pid = String.valueOf(projectInfo.get("id"));
        String pname = String.valueOf(projectInfo.get("pname"));

        // 获取通讯机信息
        ArrayList<Map<String, Object>> tgList = tgDao.getTgList(pid);


        //计算安全运行天数
        long safe_day = DateUtil.getDayOfTwoDate(DateUtil.parseStrToDate(String.valueOf(projectInfo.get("date_time")), "yyyy-MM-dd"), new Date());
        projectInfo.put("safe_day", safe_day);

        // 获取用户权限列表
        Map<String, Object> authListMap = new HashMap<>();
            //菜单权限
        ArrayList<Map<String, Object>> menuList = authDao.getMenuList(pid, uid);
        if(menuList == null || menuList.size() == 0){
            menuList = authDao.getMenuList2(uid);
        }
            //视图权限
        ArrayList<String> viewList = authDao.getViewList(uid);
            //数据权限
        ArrayList<Map<String, Object>> dataList = authDao.getDataList(uid);
        authListMap.put("menu", menuList);
        authListMap.put("view", viewList);
        authListMap.put("data", dataList);

        //获取全部需要验证的view
        ArrayList<String> needAuthView = authDao.getNeedAuthView();

        //获取厂区信息
        ArrayList<Map<String, Object>> authFactorys = userDao.getAuthFactorys(pid, uid);
        //遍历厂区给厂区添加
        Map<String, Object> param = new HashMap<>();
        for (Map<String, Object> each : authFactorys) {
            param.put("factory_id", String.valueOf(each.get("id")));
            param.put("room_name", "transformerroom");
            //获取当前厂区下全部配电室
            List<Map<String, Object>> transformerRoomList = userDao.getRoomList(param);
            each.put("transformerroom_list", transformerRoomList.stream()
                    .map(item -> String.valueOf(item.get("id")))
                    .collect(Collectors.toList()));

            //获取当前厂区下全部水泵房
            param.put("room_name", "water_pump_room");
            List<Map<String, Object>> waterPumpList = userDao.getRoomList(param);
            each.put("waterpump_list", waterPumpList.stream()
                    .map(item -> String.valueOf(item.get("id")))
                    .collect(Collectors.toList()));

            //获取当前厂区下全部配气室
            param.put("room_name", "gas_pressure_room");
            List<Map<String, Object>> pressureRoomList = userDao.getRoomList(param);
            each.put("pressureroom_list", pressureRoomList.stream()
                    .map(item -> String.valueOf(item.get("id")))
                    .collect(Collectors.toList()));
        }

        // 更新url_tips表
        userDao.updateUrlTips(pid, null);

        HttpSession session = request.getSession();
        //session存储信息
        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put("user_info", userInfo);
        sessionMap.put("project_info", projectInfo);
        sessionMap.put("auth_list", authListMap);
        sessionMap.put("need_auth_view", needAuthView);
        sessionMap.put("factory_info", authFactorys);
        sessionMap.put("tg_info", tgList);
        //返回值信息
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("project_id", pid);
        returnMap.put("uname", userInfo.get("uname"));
        returnMap.put("ove_session", session.getId());

        session.setAttribute("uid", uid);
        session.setAttribute("pid", pid);
        session.setAttribute("userInfo", sessionMap);
        session.setAttribute("tg_info", tgList);
        session.setAttribute("pname", pname);
        session.setAttribute("project_info", projectInfo);
//        if(!request.isRequestedSessionIdValid()){
            if(authFactorys != null && authFactorys.size() > 0){
                session.setAttribute("factory_id", authFactorys.get(0).get("id"));
            }
//            String key = "ove_session:" + session.getId();
//            redisUtil.set(key, JSON.toJSONString(sessionMap), 4);
//        }

        //记录日志
        recordLog("login", request);

        return WebResult.success(returnMap);
    }

    @Override
    public WebResult logout(HttpServletRequest request){
        if(request.isRequestedSessionIdValid()){
            HttpSession session = request.getSession();
            //记录日志
            recordLog("logout", request);
            session.invalidate();
        }
        return WebResult.success();
    }

    //显示登录页信息
    @Override
    public WebResult loginpage(String hostname){
        Map<String, Object> userLoginPage = userDao.getUserLoginPage(hostname);
        if(userLoginPage != null && userLoginPage.size() > 0){
            return WebResult.success(userLoginPage);
        }else{
            return WebResult.errorData(207, "");
        }
    }

    //修改用户密码
    @Transactional
    @Override
    public WebResult updatePwd(HttpServletRequest request, Map<String,Object> map){
        String uname = String.valueOf(map.get("uname"));
        String pwd = String.valueOf(map.get("pwd"));
        String oldPwd = String.valueOf(map.get("old_pwd"));
        uname = StringUtils.trim(uname);
        if(StringUtils.isEmpty(uname) || StringUtils.isEmpty(pwd) || StringUtils.isEmpty(oldPwd) || uname.length() < 1 || uname.length() > 12){
            return WebResult.error(301);
        }
        Map<String, Object> userInfo = userDao.getUserInfo(uname);
        if(userInfo == null){
            return WebResult.error(603);
        }
        if(pwd.equals(oldPwd)){
            return WebResult.error(604);
        }
        if(userInfo == null || !oldPwd.equals(String.valueOf(userInfo.get("pwd")))){
            return WebResult.error(605);
        }

        //记录日志
        recordLog("update_pwd", request);

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("id", userInfo.get("uid"));
        paramsMap.put("pwd", pwd);
        paramsMap.put("update_time", new Date());

       int result = userDao.updatePwd(paramsMap);
       if(result > 0){
           return WebResult.success(200, "修改成功");
       }else {
           return WebResult.error(201);
       }

    }

    //记录日志
    private void recordLog(String oper, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String uid = String.valueOf(session.getAttribute("uid"));
        String pid = String.valueOf(session.getAttribute("pid"));
        Map<String, Object> params = new HashMap<>();
        params.put("uid", uid);
        params.put("oper", oper);
        params.put("oper_time", new Date());
        params.put("ip", request.getRemoteHost());
        params.put("pid", pid);
        userDao.addUserLog(params);
    }
}
