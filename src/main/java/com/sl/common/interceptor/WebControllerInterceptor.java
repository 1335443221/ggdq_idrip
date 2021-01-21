package com.sl.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sl.common.exception.WebMyException;
import com.sl.common.utils.RedisUtil;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.config.MySession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author 李旭日
 * 拦截Web的控制器
 * 拦截器 @ 用来判断用户的
 * 1.当preHandle方法返回false时，从当前拦截器往回执行所有拦截器的afterCompletion方法，再退出拦截器链。也就是说，请求不继续往下传了，直接沿着来的链往回跑。
 * 2.当preHandle方法全为true时，执行下一个拦截器,直到所有拦截器执行完。再运行被拦截的Controller。然后进入拦截器链，运
 * 行所有拦截器的postHandle方法,完后从最后一个拦截器往回执行所有拦截器的afterCompletion方法.
 */
@Component
public class WebControllerInterceptor implements HandlerInterceptor {

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     *
     * @param request
     * @param response
     * @param handler
     * @throws Exception
     */
    @Autowired
    MySession mySession;

    @Autowired
    RedisUtil redisUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //无论访问的地址是不是正确的，都进行登录验证，登录成功后的访问再进行分发，404的访问自然会进入到错误控制器中
        if (request.isRequestedSessionIdValid()) {//验证session可用性
            // 权限过滤
            // 把参数封裝成map   //得到枚举类型的参数名称，参数名称若有重复的只能得到第一个
            Map<String, Object> map = getParamOfMap(request, new HashMap<String, Object>());
            String ContentType=request.getContentType();
            //穿application/json格式时  把json串当做一个key存到map中 key名字为json
            if(ContentType!=null&&ContentType.equals("application/json")){
                WebMyRequestWrapper requestWrapper = new WebMyRequestWrapper(request);
                String body = requestWrapper.getBody();
                //处理json数据
                JSONObject bodyData = JSONObject.parseObject(body);
                for(String key : bodyData.keySet()){
                    map.put(key, bodyData.get(key));
                }
//                map.put("json",body);
            }
            return authInterceptor(request, response, map);
        }else{
            // url 错误 404
            if ("/error".equals(request.getRequestURI())) {
                printContent(response, JSON.toJSONString(WebResult.error(404)));
            }else{
                //登录过期或者未登录，需要重新登录
                printContent(response, JSON.toJSONString(WebResult.error(450,"登录已过期!","")));
            }
            return false;
        }
    }


    /**
     * 权限过滤（menu、view）&获取项目id
     * @param request
     * @param response
     * @param map  参数集合
     */
    private boolean authInterceptor(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        try {
            // url 错误 404
            if ("/error".equals(request.getRequestURI())) {
                throw new WebMyException(404);
            }
            String[] uriArr = request.getRequestURI().split("/");
            String uri = request.getRequestURI().substring(1);
            //获取用户信息
            Map<String, Object> userInfoMap = mySession.getUserInfo(request);
            // 判断是否有本控制器访问权限
            if (userInfoMap.get("auth_list") == null) {
                throw new WebMyException(206,String.valueOf(userInfoMap));
            }
            //拥有的权限
            Map<String, Object> auth_list_map = JSONObject.parseObject(String.valueOf(userInfoMap.get("auth_list")));

            //如果是公共方法  则不需要判断菜单
            if (!"common".equals(uriArr[1])
                    &&!"webCommon".equals(uriArr[1])
                    &&!"Common".equals(uriArr[1])
                    &&!"export".equals(uriArr[1])){
                //验证menu
                List<Map> menu = JSONArray.parseArray(String.valueOf(auth_list_map.get("menu")), Map.class);
                List<String> uriList = new ArrayList<>();
                for(Map each : menu){
                    uriList.add(String.valueOf(each.get("uri")));
                }
                uriList.add("login");
                if (menu == null || !uriList.contains(uriArr[1])) {
                    throw new WebMyException(206,String.valueOf(userInfoMap));
                }
            }

            // 需要验证的view权限map
            List<String> need_view_map = JSONArray.parseArray(String.valueOf(userInfoMap.get("need_auth_view")), String.class);
            //如果需要验证的view包含当前访问的url
            if (need_view_map.contains(uri)){
                //如果view是空  返回没权限
                if ("".equals(String.valueOf(auth_list_map.get("view"))) || "null".equals(String.valueOf(auth_list_map.get("view")))) {
                    throw new WebMyException(206,String.valueOf(userInfoMap));
                }else{
                    //该用户已赋权view
                    List<String> view = new ArrayList<>();
                    if(!String.valueOf(auth_list_map.get("view")).equals("[]")){
                        view = JSONArray.parseArray(String.valueOf(auth_list_map.get("view")), String.class);
                    }
                    //如果该用户的权限中没有当前访问的url  返回没权限
                    if (!view.contains(uri)) {
                        throw new WebMyException(205,String.valueOf(userInfoMap));
                    }
                }
            }



          //项目信息
          Map<String, Object> project_info = JSONObject.parseObject((String.valueOf(userInfoMap.get("project_info"))));
          Map<String, Object> user_info = JSONObject.parseObject((String.valueOf(userInfoMap.get("user_info"))));

            //判断配电室、水泵房等 权限
            if (map.get("room_type")!=null&&map.get("room_id")!=null&&!map.get("room_type").toString().equals("")){
                String room_type= map.get("room_type").toString();
                Map<String, List<String>> authData=mySession.getAuthData(request);
                List<String> roomIdList= new ArrayList<>();
//                String roomName="";
                switch (room_type){
                    case "1":  //电
                        roomIdList= authData.get("transformerroom");
//                        roomName="配电室";
                        break;
                    case "2":   //水
                        roomIdList= authData.get("waterpump");
//                        roomName="水泵房";
                        break;
                    case "3":   //气
                        roomIdList= authData.get("pressureroom");
//                        roomName="调压站";
                        break;
                }
                //证明没权限
                //没传room_id  拿第一个
                if (map.get("room_id").toString().equals("")&&roomIdList.size()>0){
                    map.put("room_id",roomIdList.get(0));
                }
                //不等于空  也不等于零
                if (!map.get("room_id").toString().equals("")&&!map.get("room_id").toString().equals("0")){
                    //属于int类型
                    if (map.get("room_id") instanceof Integer ){
                        if (!roomIdList.contains(map.get("room_id").toString())){
                            throw new WebMyException(404,"网络异常！",String.valueOf(userInfoMap));
                        }
                    }
                    //属于List类型  去除没有权限的id
                    if (map.get("room_id") instanceof List ){
                        List<String> roomList =(List)map.get("room_id");
                        for (int i=0;i<roomList.size();i++){
                            if (!roomIdList.contains(roomList.get(i))){
                                roomList.remove(roomList.get(i));
                            }
                        }
                        if (roomList.size()==0){
                            throw new WebMyException(404,"网络异常！",String.valueOf(userInfoMap));
                        }
                        map.put("room_id",roomList);
                    }

                    //以，相隔的String类型
                    if (map.get("room_id") instanceof String ){
                        List<String> roomList = Arrays.asList(map.get("room_id").toString().split(","));
                        StringBuffer roomIds = new StringBuffer("");;
                        for (int i=0;i<roomList.size();i++){
                            if (roomIdList.contains(roomList.get(i))){ //如果包含
                                roomIds.append(roomList.get(i) + ",");;
                            }
                        }
                        if (roomIds.length()>0) {
                            if (roomIds.substring(roomIds.length() - 1).equals(",")) {
                                roomIds.deleteCharAt(roomIds.length() - 1);
                            }
                        }else{
                            throw new WebMyException(404,"网络异常！",String.valueOf(userInfoMap));
                        }
                        map.put("room_id",roomIds.toString());
                    }

                }
            }


          if(map.get("project_id")==null) map.put("project_id", project_info.get("id"));
          if(map.get("project_name")==null) map.put("project_name", project_info.get("code_name"));
          if(map.get("code_name")==null) map.put("code_name", project_info.get("code_name"));
          if(map.get("user_id")==null) map.put("user_id", user_info.get("uid"));
          if(map.get("user_name")==null) map.put("user_name", user_info.get("name"));
          // 项目id
          request.setAttribute("project_id",map.get("project_id"));
          //项目名称
          request.setAttribute("project_name",map.get("project_name"));
          //项目的MongoDB名称
          request.setAttribute("code_name",map.get("code_name"));
          //登录人的user_id
          request.setAttribute("user_id",map.get("user_id"));
          //登录人的user_name
          request.setAttribute("user_name",map.get("user_name"));
        } finally {
          request.setAttribute("map", map);// Controller Param map
        }
        return true;
    }



    private Map<String, Object> getParamOfMap(HttpServletRequest request, Map<String, Object> map) {

        // 得到枚举类型的参数名称，参数名称若有重复的只能得到第一个
        Enumeration enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String paramName = (String) enums.nextElement();
            String paramValue = request.getParameter(paramName);
            // 形成键值对应的map
            map.put(paramName, paramValue);
            request.setAttribute(paramName,paramValue);
        }
        return map;
    }


    public void setRequest(HttpServletRequest request, HttpServletResponse response) {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            String reqHeaders = req.getHeader("Access-Control-Request-Headers");
            String originHeader = req.getHeader("Origin");
            resp.setHeader("Access-Control-Allow-Origin", originHeader);
            resp.setHeader("Access-Control-Allow-Methods", "GET, POST, HEAD, TRACE, PUT, DELETE, OPTIONS, CONNECT");
            if (StringUtils.isEmpty((reqHeaders))) {
                resp.setHeader("Access-Control-Allow-Headers", "Content-Type, x_requested_with, *");
            } else {
                resp.setHeader("Access-Control-Allow-Headers", reqHeaders);
            }
            resp.setHeader("Access-Control-Max-Age", "3600");
            resp.setHeader("Access-Control-Allow-Credentials", "true");
            if ("OPTIONS".equals(req.getMethod()))
                return;
        }
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     *
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // System.out.println("拦截器postHandle方法"+modelAndView);

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // hostHolder.clear(); //当执行完成之后呢需要将变量清空
    }

    private static void printContent(HttpServletResponse response, String content) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter pw = response.getWriter();
            pw.append(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
