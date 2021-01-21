package com.sl.idripweb.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sl.common.service.CacheService;
import com.sl.common.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过session获取用户信息
 *
 * @author msw
 */
@Service("mySession")
public class MySession {

    @Autowired
    private CacheService cacheServiceImpl;
    @Autowired
    private DateUtil dateUtil;

    private Logger logger = LoggerFactory.getLogger(MySession.class);

    // 获取用户信息
    public Map<String, Object> getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return JSONObject.parseObject(JSON.toJSONString(session.getAttribute("userInfo")), Map.class);
    }

    //获取项目信息
    public Map<String, Object> getProjectInfo(HttpServletRequest request){
        HttpSession session = request.getSession();
        return JSONObject.parseObject(JSON.toJSONString(session.getAttribute("project_info")), Map.class);
    }

    //获取项目id
    public String getPid(HttpServletRequest request){
        HttpSession session = request.getSession();
        return String.valueOf(session.getAttribute("pid"));
    }

    //获取厂区id
    public String getFactoryId(HttpServletRequest request){
        HttpSession session = request.getSession();
        return String.valueOf(session.getAttribute("factory_id"));
    }

    //获取所有厂区信息
    public List<Map> getFactoryInfo(HttpServletRequest request){
        Map<String, Object> userInfo = getUserInfo(request);
        return JSONArray.parseArray(JSON.toJSONString(userInfo.get("factory_info")), Map.class);
    }

    //获取有权限的菜单ids
    public List<String> getAuthIds(HttpServletRequest request){
        Map<String, Object> userInfo = getUserInfo(request);
        JSONObject authList = JSONObject.parseObject(JSON.toJSONString(userInfo.get("auth_list")));
        List<Map> menuList = JSONArray.parseArray(JSON.toJSONString(authList.get("menu")), Map.class);
        List<String> authIds = new ArrayList<>();
        menuList.forEach(each ->authIds.add(String.valueOf(each.get("id"))));
        return authIds;
    }

    //获取所有的menu_list
    public List<Map> getMenuList(HttpServletRequest request){
        Map<String, Object> userInfo = getUserInfo(request);
        JSONObject authList = JSONObject.parseObject(JSON.toJSONString(userInfo.get("auth_list")));
        return JSONArray.parseArray(JSON.toJSONString(authList.get("menu")), Map.class);
    }

    //通过厂区id获取单个厂区信息
    public Map getFactoryById(HttpServletRequest request, String factoryId){
        List<Map> factoryInfo = getFactoryInfo(request);
        for(Map each : factoryInfo){
            if(factoryId.equals(each.get("id").toString())) return each;
        }
        return null;
    }

    //获取项目时间
    public String getProjectTime(HttpServletRequest request){
        Map<String, Object> userInfo = getUserInfo(request);
        JSONObject projectInfo = JSONObject.parseObject(JSON.toJSONString(userInfo.get("project_info")));
        String dateTime = String.valueOf(projectInfo.get("date_time"));
        return dateUtil.parseTimestampToStr(Long.parseLong(dateTime), "yyyy-MM-dd HH;mm:ss");
    }

    //获取key-value形式的厂区信息
    public Map<String, Object> getProjectFactorys(HttpServletRequest request){
        Map<String, Object> projectFactorys = new HashMap<>();
        List<Map> factoryInfo = getFactoryInfo(request);
        factoryInfo.forEach(each -> {
            projectFactorys.put(String.valueOf(each.get("id")), each);
        });
        return projectFactorys;
    }

    //获取auth_list中的data权限
    public Map<String, List<String>> getAuthData(HttpServletRequest request){
        Map<String, Object> userInfo = getUserInfo(request);
        JSONObject authList = JSONObject.parseObject(JSON.toJSONString(userInfo.get("auth_list")));
        List<Map> dataList = JSONArray.parseArray(JSON.toJSONString(authList.get("data")), Map.class);
        Map<String, List<String>> dataMap = new HashMap<>();
        dataMap.put("transformerroom", new ArrayList<>());
        dataMap.put("waterpump", new ArrayList<>());
        dataMap.put("pressureroom", new ArrayList<>());
        for(Map each : dataList){
            String dataType = String.valueOf(each.get("data_type"));
            if("transformerroom".equals(dataType)){
                dataMap.get("transformerroom").add(String.valueOf(each.get("data_id")));
            }else if ("waterpump".equals(dataType)){
                dataMap.get("waterpump").add(String.valueOf(each.get("data_id")));
            }else {
                dataMap.get("pressureroom").add(String.valueOf(each.get("data_id")));
            }
        }
        return dataMap;
    }

}
