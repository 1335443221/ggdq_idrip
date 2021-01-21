package com.sl.common.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.sl.common.config.ConstantConfig;
import com.sl.common.dao.CommonDao;
import com.sl.common.entity.CategoryTree;
import com.sl.common.entity.OperationLogs;
import com.sl.common.entity.params.SetValParams;
import com.sl.common.exception.WebMyException;
import com.sl.common.utils.RedisUtil;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.config.MySession;
import com.sl.idripweb.dao.UserDao;
import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.net.*;
/**
 * Created by IntelliJ IDEA.
 * Author: 李旭日
 * Date: 2020/9/30 15:48
 * FileName: CommonService
 * Description: 公共业务层(APP+WEB)
 */

@Service
public class CommonService {
    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private ConstantConfig constantConfig;
    @Autowired
    private RedisUtil redisPoolUtil;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private MySession mySession;
    @Autowired
    private UserDao userDao;


    /**
     * 获取变压器列表
     * @return
     */
    public WebResult getTransformerList(Map<String, Object> map) {
        return WebResult.success(commonDao.getTransformerList(map));
    }


    /**
     * 获取树结构/水电气
     * @return
     */
    public WebResult getCategoryTree(Map<String, Object> map, HttpServletRequest request) {
        if (map.get("room_id")==null||map.get("room_id").toString().equals("")){
            map.put("room_id",0);
        }
        if (map.get("factory_id")==null||map.get("factory_id").toString().equals("")){
            map.put("factory_id",mySession.getFactoryId(request));
        }
        //配电室类型   1电、2水、3气
        switch (String.valueOf(map.get("room_type"))) {
            case "1":
                map.put("categoryRelationTable", "elec_project_category_relation");  // category
                map.put("roomIdName", "transformerroom_id");  // 配电室名称id
                break;
            case "2":
                map.put("categoryRelationTable", "water_project_category_relation");  // category
                map.put("roomIdName", "water_pump_room_id");  // 配电室名称id
                break;
            case "3":
                map.put("categoryRelationTable", "water_project_category_relation");  // category
                map.put("roomIdName", "water_pump_room_id");  // 配电室名称id
                /*
                map.put("categoryRelationTable", "gas_project_category_relation");  // category
                map.put("roomIdName", "gas_pressure_room_id");  // 配电室名称id*/
                break;
        }
        List<CategoryTree> categoryList=commonDao.getCategoryTree(map);
        CategoryTree treeUtil=new CategoryTree();
        List<CategoryTree> categoryTree=treeUtil.getCategoryTreeByRecursion("0",categoryList);
        Map<String,Object> result=new LinkedHashMap<>(5);
        result.put("project_id",map.get("project_id"));
        result.put("factory_id",map.get("factory_id"));
        result.put("transformerroom_id",map.get("room_id"));
        result.put("category_type",map.get("category_type"));
        result.put("data_list",categoryTree);
        return WebResult.success(result);
    }

    /**
     * 获取主页面4个菜单
     * @param request
     * @return
     */
    public WebResult goMainPage(HttpServletRequest request){
        Map<String, Object> params = new HashMap<>();
        params.put("pid", 0);
        List<Map<String, Object>> menuList = commonDao.getMenuList(params);
        List<String> authIds = mySession.getAuthIds(request);
        menuList.forEach(each -> {
            String id = String.valueOf(each.get("id"));
            each.put("has_auth", authIds.contains(id));
            Map<String, Object> meta = new HashMap<>();
            meta.put("title", each.get("name"));
            meta.put("icon", each.get("icon"));
            each.put("meta", meta);
        });
        if(menuList == null || menuList.size() == 0){
            return WebResult.error(207);
        }else{
            return WebResult.success(menuList);
        }
    }

    /**
     * 获取菜单树
     * @param request
     * @param map
     * @return
     */
    public WebResult mainNav(HttpServletRequest request, Map<String, Object> map) {
        Integer parentMenuId = 0;
        if(map.get("parent_menu_id") != null) parentMenuId = Integer.parseInt(String.valueOf(map.get("parent_menu_id")));
        List<Map> menuList = mySession.getMenuList(request);
        //移除不需要展示的menu菜单
        menuList.removeIf(each -> {
            String display = String.valueOf(each.get("display"));
            return "0".equals(display);
        });
        if(menuList.size() == 0){
            return WebResult.error(207);
        }else{
            int maxLevel = 0;
            List<Map<String, Object>> menuTree = getMenuTree(menuList, parentMenuId, 1, maxLevel);
            Map<String, Object> result = new HashMap<>();
            result.put("menu_tree", menuTree);
            return WebResult.success(result);
        }
    }

    /**
     * 获取session中的项目厂区信息
     * @param request
     * @return
     */
    public WebResult getFactoryByProject(HttpServletRequest request){
        List<Map> factoryInfo = mySession.getFactoryInfo(request);
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> factories = new ArrayList<>();
        factoryInfo.forEach(each -> {
            Map<String, Object> factory = new HashMap<>();
            factory.put("id", each.get("id"));
            factory.put("name", each.get("factory_name"));
            factories.add(factory);
        });
        result.put("data_list", factories);
        return WebResult.success(result);
    }

    /**
     * 获取room列表
     * @param request
     * @return
     */
    public WebResult getRoomByFactory(HttpServletRequest request, Map<String, Object> map){
        String factoryId ="";
        if( map.get("factory_id")==null||String.valueOf(map.get("factory_id")).equals("")){
            factoryId=mySession.getFactoryId(request);
        }else{
            factoryId=String.valueOf(map.get("factory_id"));
        }
        String roomTypeId = map.get("room_type") != null ? String.valueOf(map.get("room_type")) : "0";
        String isAll = map.get("is_all") != null ? String.valueOf(map.get("is_all")) : "0";
        //获取session中的厂区信息
        Map factoryById = mySession.getFactoryById(request, factoryId);
        List<String> transformerroomList = JSONArray.parseArray(JSON.toJSONString(factoryById.get("transformerroom_list")), String.class);
        List<String> waterpumpList = JSONArray.parseArray(JSON.toJSONString(factoryById.get("waterpump_list")), String.class);
        List<String> pressureroomList = JSONArray.parseArray(JSON.toJSONString(factoryById.get("pressureroom_list")), String.class);
        //获取authList
        Map<String, List<String>> authData = mySession.getAuthData(request);
        List<String> transformerroomDataIds = authData.get("transformerroom");
        List<String> waterpumpDataIds = authData.get("waterpump");
        List<String> pressureroomDataIds = authData.get("pressureroom");
        List<String> roomIds = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        switch (roomTypeId){
            case "1":
                transformerroomList.retainAll(transformerroomDataIds);
                roomIds = transformerroomList;
                param.put("room_name", "transformerroom");
                break;
            case "2":
                waterpumpList.retainAll(waterpumpDataIds);
                roomIds = waterpumpList;
                param.put("room_name", "water_pump_room");
                break;
            case "3":
                pressureroomList.retainAll(pressureroomDataIds);
                roomIds = pressureroomList;
                param.put("room_name", "gas_pressure_room");
                break;
        }
        if(roomIds.size() == 0){
            return WebResult.error(301);
        }
        param.put("room_ids", roomIds);
        param.put("factory_id", factoryId);
        List<Map<String, Object>> roomList = userDao.getRoomList(param);
        if(!StringUtils.isEmpty(isAll) && "1".equals(isAll)){
            Map<String, Object> all = new HashMap<>();
            all.put("id", 0);
            all.put("name", "全部");
            roomList.add(0, all);
        }
        if(roomList.size() == 0){
            return WebResult.error(207);
        }else{
            Map<String, Object> result = new HashMap<>();
            result.put("data_list", roomList);
            return WebResult.success(result);
        }
    }
    /**
     * 验证敏感操作密码
     * @param request
     * @return
     */
    public WebResult verifyPwd(HttpServletRequest request, Map<String, Object> map){
        if (map.get("oper_pwd")==null||map.get("oper_pwd").toString().equals("")){
            return WebResult.error(301);
        }
        Map<String, Object> pwdMap=commonDao.verifyPWD(map);
        if (pwdMap==null){
            return WebResult.error(202);
        }
        Map<String, Object> data_list=new HashMap<>();
        data_list.put("verify_str",map.get("oper_pwd"));
        pwdMap.put("data_list",data_list);
        return WebResult.success(pwdMap);
    }


    /**
     * 验证操作密码并下置并记录操作日志
     * @param request
     * @return
     */
    public WebResult setValue(HttpServletRequest request, Map<String, Object> map){
        if (map.get("oper_pwd")==null||map.get("oper_pwd").toString().equals("")){
            return WebResult.error(301);
        }
        Map<String, Object> pwdMap=commonDao.verifyPWD(map);
        if (pwdMap==null){
            return WebResult.error(202);
        }
        //操作日志
        OperationLogs operationLogs=new OperationLogs();
        //下置参数
        SetValParams setValParams =new SetValParams();
        setValParams.setProjectId(Integer.parseInt(String.valueOf(map.get("project_id"))));
        setValParams.setFactoryId(Integer.parseInt(String.valueOf(map.get("factory_id"))));
        setValParams.setTgName(String.valueOf(map.get("tg_name")));
        setValParams.setTag(String.valueOf(map.get("tag")));
        setValParams.setVal(String.valueOf(map.get("val")));

        operationLogs.setUid(Integer.parseInt(String.valueOf(map.get("user_id"))));
        operationLogs.setProject_id(Integer.parseInt(String.valueOf(map.get("project_id"))));

        //获取本机ip
        String localip="";
        try {
            InetAddress ia=InetAddress.getLocalHost();
            localip=ia.getHostAddress();
        } catch (Exception e) {
             e.printStackTrace();
        }
        operationLogs.setIp(localip);
        operationLogs.setClient("web");
        operationLogs.setRequest_uri("common/setValue");
        operationLogs.setDetailed_type(String.valueOf(map.get("detailed_type")));
        //行为类型
        String behavior_type=String.valueOf(map.get("behavior_type"));
        Map<String,Object> deviceParams=new HashMap<>();
        String tag= String.valueOf(map.get("tag"));
        deviceParams.put("meterName",tag.split("_")[0]  + "_" +tag.split("_")[1]);
        deviceParams.put("project_id",String.valueOf(map.get("project_id")));
        deviceParams.put("factory_id",String.valueOf(map.get("factory_id")));
        switch (behavior_type){
            case "elec_control":
                deviceParams.put("device_type_id",1);
                deviceParams.put("tableName","elec_meter");
                operationLogs.setBehavior_type("电表控制");
                break;
            case "water_control":
                deviceParams.put("device_type_id",4);
                deviceParams.put("tableName","water_meter");
                operationLogs.setBehavior_type("水表控制");
                break;
            case "air_conditioning_control":
                deviceParams.put("device_type_id",11);
                deviceParams.put("tableName","air_conditioner_meter");
                operationLogs.setBehavior_type("空调控制");
                break;
            case "light_control":
                deviceParams.put("device_type_id",14);
                deviceParams.put("tableName","light");
                operationLogs.setBehavior_type("照明控制");
                break;
            case "cooling_system_control":
                deviceParams.put("device_type_id",15);
                deviceParams.put("tableName","cooling_system_device");
                operationLogs.setBehavior_type("冷凝控制");
                break;
            case "door_control":
                deviceParams.put("device_type_id",0);
                operationLogs.setBehavior_type("门禁控制");
                break;
        }
        if (!behavior_type.equals("door_control")){
            operationLogs.setDevice_type_relation_id(commonDao.getDeviceIdByName(deviceParams));
        }
        setValParams.setOperationLogs(operationLogs);
        setVal(setValParams);
        return WebResult.success(setValParams);
    }


    /**
     * 验证操作密码并下置并记录操作日志
     * @param request
     * @return
     */
    public WebResult setXuansiVal(HttpServletRequest request, Map<String, Object> map){
        if (map.get("verify_str")==null||map.get("verify_str").toString().equals("")){
            return WebResult.error(301);
        }
        map.put("oper_pwd",map.get("verify_str"));
        Map<String, Object> pwdMap=commonDao.verifyPWD(map);
        if (pwdMap==null){
            return WebResult.error(202);
        }
        //操作日志
        OperationLogs operationLogs=new OperationLogs();
        //下置参数
        SetValParams setValParams =new SetValParams();
        setValParams.setProjectId(Integer.parseInt(String.valueOf(map.get("project_id"))));
        setValParams.setFactoryId(Integer.parseInt(String.valueOf(map.get("factory_id"))));
        setValParams.setTgName(String.valueOf(map.get("tg_name")));
        setValParams.setTag(String.valueOf(map.get("tag")));
        setValParams.setVal(String.valueOf(map.get("val")));

        operationLogs.setUid(Integer.parseInt(String.valueOf(map.get("user_id"))));
        operationLogs.setProject_id(Integer.parseInt(String.valueOf(map.get("project_id"))));

        //获取本机ip
        String localip="";
        try {
            InetAddress ia=InetAddress.getLocalHost();
            localip=ia.getHostAddress();
        } catch (Exception e) {
             e.printStackTrace();
        }
        operationLogs.setIp(localip);
        operationLogs.setClient("web");
        operationLogs.setRequest_uri("common/set_xuansi_val");
        operationLogs.setDetailed_type(String.valueOf(map.get("detailed_type")));
        //行为类型
        String behavior_type=String.valueOf(map.get("behavior_type"));
        Map<String,Object> deviceParams=new HashMap<>();
        String tag= String.valueOf(map.get("tag"));
        deviceParams.put("meterName",tag.split("_")[0]  + "_" +tag.split("_")[1]);
        deviceParams.put("project_id",String.valueOf(map.get("project_id")));
        deviceParams.put("factory_id",String.valueOf(map.get("factory_id")));
        switch (behavior_type){
            case "elec_control":
                deviceParams.put("device_type_id",1);
                deviceParams.put("tableName","elec_meter");
                operationLogs.setBehavior_type("电表控制");
                break;
            case "water_control":
                deviceParams.put("device_type_id",4);
                deviceParams.put("tableName","water_meter");
                operationLogs.setBehavior_type("水表控制");
                break;
            case "air_conditioning_control":
                deviceParams.put("device_type_id",11);
                deviceParams.put("tableName","air_conditioner_meter");
                operationLogs.setBehavior_type("空调控制");
                break;
            case "light_control":
                deviceParams.put("device_type_id",14);
                deviceParams.put("tableName","light");
                operationLogs.setBehavior_type("照明控制");
                break;
            case "cooling_system_control":
                deviceParams.put("device_type_id",15);
                deviceParams.put("tableName","cooling_system_device");
                operationLogs.setBehavior_type("冷凝控制");
                break;
            case "door_control":
                deviceParams.put("device_type_id",0);
                operationLogs.setBehavior_type("门禁控制");
                break;
        }
        if (!behavior_type.equals("door_control")){
            operationLogs.setDevice_type_relation_id(commonDao.getDeviceIdByName(deviceParams));
        }
        setValParams.setOperationLogs(operationLogs);
        setVal(setValParams);
        return WebResult.success(setValParams);
    }

    /**
     * 验证操作密码并下置并记录操作日志
     * @param request
     * @return
     */
    public WebResult setXuansiVal2(HttpServletRequest request, Map<String, Object> map){
        //操作日志
        OperationLogs operationLogs=new OperationLogs();
        //下置参数
        SetValParams setValParams =new SetValParams();
        setValParams.setProjectId(Integer.parseInt(String.valueOf(map.get("project_id"))));
        setValParams.setFactoryId(Integer.parseInt(String.valueOf(map.get("factory_id"))));
        setValParams.setTgName(String.valueOf(map.get("tg_name")));
        setValParams.setTag(String.valueOf(map.get("tag")));
        setValParams.setVal(String.valueOf(map.get("val")));

        operationLogs.setUid(Integer.parseInt(String.valueOf(map.get("user_id"))));
        operationLogs.setProject_id(Integer.parseInt(String.valueOf(map.get("project_id"))));

        //获取本机ip
        String localip="";
        try {
            InetAddress ia=InetAddress.getLocalHost();
            localip=ia.getHostAddress();
        } catch (Exception e) {
             e.printStackTrace();
        }
        operationLogs.setIp(localip);
        operationLogs.setClient("web");
        operationLogs.setRequest_uri("common/set_xuansi_val2");
        operationLogs.setDetailed_type(String.valueOf(map.get("detailed_type")));
        //行为类型
        String behavior_type=String.valueOf(map.get("behavior_type"));
        Map<String,Object> deviceParams=new HashMap<>();
        String tag= String.valueOf(map.get("tag"));
        deviceParams.put("meterName",tag.split("_")[0]  + "_" +tag.split("_")[1]);
        deviceParams.put("project_id",String.valueOf(map.get("project_id")));
        deviceParams.put("factory_id",String.valueOf(map.get("factory_id")));
        switch (behavior_type){
            case "elec_control":
                deviceParams.put("device_type_id",1);
                deviceParams.put("tableName","elec_meter");
                operationLogs.setBehavior_type("电表控制");
                break;
            case "water_control":
                deviceParams.put("device_type_id",4);
                deviceParams.put("tableName","water_meter");
                operationLogs.setBehavior_type("水表控制");
                break;
            case "air_conditioning_control":
                deviceParams.put("device_type_id",11);
                deviceParams.put("tableName","air_conditioner_meter");
                operationLogs.setBehavior_type("空调控制");
                break;
            case "light_control":
                deviceParams.put("device_type_id",14);
                deviceParams.put("tableName","light");
                operationLogs.setBehavior_type("照明控制");
                break;
            case "cooling_system_control":
                deviceParams.put("device_type_id",15);
                deviceParams.put("tableName","cooling_system_device");
                operationLogs.setBehavior_type("冷凝控制");
                break;
            case "door_control":
                deviceParams.put("device_type_id",0);
                operationLogs.setBehavior_type("门禁控制");
                break;
        }
        if (!behavior_type.equals("door_control")){
            operationLogs.setDevice_type_relation_id(commonDao.getDeviceIdByName(deviceParams));
        }
        setValParams.setOperationLogs(operationLogs);
        setVal(setValParams);
        return WebResult.success(setValParams);
    }


    //++++++++++++++++++++++++++++++以上为对外 公共接口+++++++++++++++++++++++++++++++++++++++++++++++//


    //++++++++++++++++++++++++++++++以下为业务公共方法+++++++++++++++++++++++++++++++++++++++++++++++//

    /**
     * 根据项目id  获取所有厂区列表
     *
     * @param map project_id(必填)  factory_id(选填)
     * @return
     */
    public ArrayList<Map<String, Object>> getFactoryByPid(Map<String, Object> map) {
        return commonDao.getFactoryByProjectid(map);
    }

    /**
     * 下置命令操作  下置到旋思的通讯机
     */
    public String setVal(SetValParams setValParams) {
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        HashMap<String, Object> template = new HashMap<String, Object>();
        ArrayList<Map<String, Object>> tgInfo = commonDao.getTGInfoByName(setValParams);
        if (tgInfo == null || tgInfo.size() <= 0) {
            throw new WebMyException(606);
        }
        String boxname = tgInfo.get(0).get("tg_rtdb").toString();
        String boxsn = tgInfo.get(0).get("sn").toString();
        // 开始封装需要发送json数据
        map1.put("boxname", boxname);//TG100
        map1.put("boxsn", boxsn);
        map1.put("ctrl", "0");
        map1.put("tag", setValParams.getTag());//a1_b1_di
        map1.put("uflge", 1);
        map1.put("val", setValParams.getVal());
        map1.put("vt", 1);
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        list.add(map1);
        template.put("cmds", list);
        template.put("uuid", "abcdefg");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<HashMap<String, Object>> requestEntity = new HttpEntity<>(template, requestHeaders);
        OperationLogs operationLogs = setValParams.getOperationLogs();

        String cmds = "{}";// 成功返回值{"cmds":[{"boxname":"TG10","boxsn":"2-23002-160215-00044","ctrl":0,"inf":"操作成功","result":0,"tag":"c1_b4_kz1","uflg":0,"val":"0","vt":1}]};
        try {
            if (constantConfig.getEnvironment().equals("prod")) {
                cmds = restTemplate.postForObject(constantConfig.getXuansiIp() + "iocmd", requestEntity, String.class);
            }
            if (operationLogs != null) {
                operationLogs.setBehavior_result("成功");
                //保存操作日志
                setOperationLogs(operationLogs);
            }
        } catch (RestClientException e) {
            if (operationLogs != null) {
                operationLogs.setBehavior_result("不成功");
                //保存操作日志
                setOperationLogs(operationLogs);
            }
            throw new WebMyException(201, e.toString());
        }
        return cmds;
    }


    /**
     * 保存操作日志
     */
    public int setOperationLogs(OperationLogs operationLogs) {
        return commonDao.setOperationLogs(operationLogs);
    }


    //下置操作到自己的通讯机==
    public int webServiceSetVal(String lpName, String lpBoxSN, String lpValue) {
        try {
            String endpoint = "http://39.96.35.45:8001/netgate-server/webservice/api?wsdl";
            String targetNamespace = "http://service.webservice.admin.boot.server.spring.com";
            // 直接引用远程的wsdl文件
            // 以下都是套路
            org.apache.axis.client.Service service = new org.apache.axis.client.Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(endpoint); //api地址
            call.setOperationName(new QName(targetNamespace, "SetZValue"));// WSDL里面描述的接口名称
            call.addParameter("lpBindID", //参数名
                    XMLType.XSD_STRING, //参数类型
                    ParameterMode.IN);// 参数模式
            call.addParameter("lpSign", XMLType.XSD_STRING, ParameterMode.IN);
            call.addParameter("lpName", XMLType.XSD_STRING, ParameterMode.IN);
            call.addParameter("lpBoxSN", XMLType.XSD_STRING, ParameterMode.IN);
            call.addParameter("nIoCtrl", XMLType.XSD_STRING, ParameterMode.IN);
            call.addParameter("lpSN", XMLType.XSD_STRING, ParameterMode.IN);
            call.addParameter("lpValue", XMLType.XSD_STRING, ParameterMode.IN);

            call.setEncodingStyle("UTF-8");
            call.setReturnType(XMLType.XSD_STRING);// 设置返回类型
            String result = "{}";
            if (constantConfig.getEnvironment().equals("prod")) {
                // 给方法传递参数，并且调用方法
                result = (String) call.invoke(new Object[]{"123456", "?", lpName, lpBoxSN, 1, 1, lpValue});
            }

            return 1;
        } catch (Exception e) {
            throw new WebMyException(201, e.toString());
        }

    }


    /**
     * 获取redis实时数据
     * @param devices 设备列表(tg_id通讯机 device_name设备名 )
     * @param tagMap  标签列表(tag 标签名)
     * @return  a1_b1_qp = 50.6
     */
    public HashMap<String, Object> getRedisData(List<Map<String, Object>> devices,List<Map<String, Object>> tagMap) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            Gson gson = new Gson();
            Set<byte[]> keySet = new HashSet<byte[]>();
            for (int d = 0; d < devices.size(); d++) {
                Map<String, Object> device = devices.get(d);
                if (device == null || device.get("tg_id") == null || device.get("device_name") == null) {
                    continue;
                }
                for (int m = 0; m < tagMap.size(); m++) {
                    String _key = device.get("tg_id") + ":" + device.get("device_name") + ":" + tagMap.get(m).get("tag_name");
                    keySet.add(_key.getBytes());
                }
            }
            byte[][] values = redisPoolUtil.mget(keySet);
            byte[][] keys = keySet.toArray(new byte[keySet.size()][]);
            for (int i = 0; i < keySet.size(); ++i) {
                if (values[i] == null) continue;
                String val = new String(values[i], "utf-8");
                String key = new String(keys[i], "utf-8");
                Map<String,Object> valMap=new HashMap<>();
                valMap.put("val",df.format(Double.parseDouble(gson.fromJson(val, result.getClass()).get("val").toString())));
                result.put(key.split(":")[1]  + "_" + key.split(":")[2], valMap);            }
        } catch (Exception e) {
            throw new WebMyException(502);
        }
        return result;
    }

    /**
     * 获取redis实时数据
     * @param devices    设备列表(tg_id通讯机 device_name设备名 )
     * @param deviceType 设备类型 constantConfig中的**Meter
     * @return  a1_b1_qp = 50.6
     */
    public HashMap<String, Object> getRedisData(List<Map<String, Object>> devices, String deviceType) {
        HashMap<String, Object> result = new HashMap<>();
        List<Map<String, Object>> tagMap = cacheService.getTag(deviceType);
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            Gson gson = new Gson();
            Set<byte[]> keySet = new HashSet<byte[]>();
            for (int d = 0; d < devices.size(); d++) {
                Map<String, Object> device = devices.get(d);
                if (device == null || device.get("tg_id") == null || device.get("device_name") == null) {
                    continue;
                }
                for (int m = 0; m < tagMap.size(); m++) {
                    String _key = device.get("tg_id") + ":" + device.get("device_name") + ":" + tagMap.get(m).get("tag_name");
                    keySet.add(_key.getBytes());
                }
            }
            byte[][] values = redisPoolUtil.mget(keySet);
            byte[][] keys = keySet.toArray(new byte[keySet.size()][]);
            for (int i = 0; i < keySet.size(); ++i) {
                if (values[i] == null) continue;
                String val = new String(values[i], "utf-8");
                String key = new String(keys[i], "utf-8");
                Map<String,Object> valMap=new HashMap<>();
                valMap.put("val",df.format(Double.parseDouble(gson.fromJson(val, result.getClass()).get("val").toString())));
                result.put(key.split(":")[1]  + "_" + key.split(":")[2], valMap);
            }
        } catch (Exception e) {
            throw new WebMyException(502);
        }
        return result;
    }


    //迭代获取菜单树，生成menu树
    private List<Map<String, Object>> getMenuTree(List<Map> menuList, Integer parentMenuId, int level, int maxLevel) {
        if(menuList.size() > 0 && level > maxLevel){
            maxLevel = level;
        }
        List<Map<String, Object>> tree = new ArrayList<>();
        for(Map each : menuList){
            List<Integer> pidList = menuList.stream().map(menu -> Integer.parseInt(menu.get("pid") == null ? "-1" : String.valueOf(menu.get("pid")))).collect(Collectors.toList());
            Integer pid = Integer.parseInt(String.valueOf(each.get("pid")));
            Integer id = Integer.parseInt(String.valueOf(each.get("id")));
            if(pid.equals(parentMenuId)){
                Map<String, Object> tmp = new HashMap<>(each);
                Map<String, Object> meta = new HashMap<>();
                meta.put("title", each.get("name"));
                meta.put("icon", each.get("icon"));
                tmp.put("meta", meta);
                List<Map<String, Object>> children = new ArrayList<>();
                if(pidList.contains(id)){
                    children = getMenuTree(menuList, id, level + 1, maxLevel);
                    tmp.put("children", children);
                    // 默认展示第一个子菜单的相关信息
                    tmp.put("path", children.get(0).get("path"));
                    tmp.put("redirect", children.get(0).get("path"));
                    tmp.put("index", String.valueOf(tmp.get("path")).substring(1));
                    tree.add(tmp);
                }else{
                    tmp.put("children", children);
                    tmp.put("index", String.valueOf(tmp.get("path")).substring(1));
                    tree.add(tmp);
                }
            }
        }
        return tree;
    }

}