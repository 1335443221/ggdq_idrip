package com.sl.idripweb.service.impl;

import com.sl.common.config.ConstantConfig;
import com.sl.common.entity.params.GetMeterParams;
import com.sl.common.service.CacheService;
import com.sl.common.service.CommonService;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.dao.PowerDao;
import com.sl.idripweb.service.PowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("powerServiceImpl")
public class PowerServiceImpl implements PowerService {

    @Autowired
    private PowerDao powerDao;
    @Autowired
    private ConstantConfig constantConfig;
    @Autowired
    private CommonService commonService;
    @Autowired
    private CacheService cacheService;

    /**
     * 计量表数据（旧电路图）
     * @param map
     * @return
     */
    @Override
    public WebResult getDatav3(Map<String, Object> map) {

        //传参
        GetMeterParams getMeterParams=new GetMeterParams();
        getMeterParams.setCategoryTypeId(String.valueOf(map.get("category_type_id")));
        getMeterParams.setDeviceType(constantConfig.getElecMeter());
        getMeterParams.setProjectId(String.valueOf(map.get("project_id")));
        getMeterParams.setFactoryId(String.valueOf(map.get("factory_id")));
        getMeterParams.setTransformerroomId( String.valueOf(map.get("room_id")));
        //获取电表设备 实时数据  查询数据库走缓存
        List<Map<String, Object>> devices = cacheService.getMeterInfo(getMeterParams);
        Map<String, Object> redisData=commonService.getRedisData(devices,constantConfig.getElecMeter());

        Map<String, Object> result=new HashMap<>(2);
        result.put("device",devices);
        result.put("values",redisData);
        return WebResult.success(result);
    }
    /**
     * 计量表数据（新电路图）
     * @param map
     * @return
     */
    @Override
    public WebResult getDeviceMonitor(Map<String, Object> map) {

        //传参
        GetMeterParams getMeterParams=new GetMeterParams();
        getMeterParams.setCategoryTypeId(String.valueOf(map.get("category_type_id")));
        getMeterParams.setDeviceType(constantConfig.getElecMeter());
        getMeterParams.setProjectId(String.valueOf(map.get("project_id")));
        getMeterParams.setFactoryId(String.valueOf(map.get("factory_id")));
        getMeterParams.setTransformerroomId( String.valueOf(map.get("room_id")));
        //获取电表设备 实时数据  查询数据库走缓存
        List<Map<String, Object>> devices = cacheService.getMeterInfo(getMeterParams);
        Map<String, Object> redisData=commonService.getRedisData(devices,constantConfig.getElecMeter());
        //遍历redis数据  输出结果： a1_b1:{"id":{"val":"1"},"ua":{"val":"1"}}
        Map<String, Map<String, Object>> redisData2=new HashMap<>();
        for ( Map.Entry<String, Object> entry:redisData.entrySet()){
            String[] keyList=entry.getKey().split("_");// a1  b1  ua
            String deviceName=keyList[0]+"_"+keyList[1];  //a1_b1
            Map<String, Object> value=(Map<String, Object>)entry.getValue();
            value.put("is_change",1);
            if (redisData2.get(deviceName)!=null){
                redisData2.get(deviceName).put(keyList[2],value);
            }else{
                Map<String, Object> valueData=new HashMap<>();
                valueData.put(keyList[2],value);
                redisData2.put(deviceName,valueData);
            }
        }
        //把实时数据 放到 devices中
        Map<String,Map<String, Object>> devicesMap=new HashMap<>();
        for (int i=0;i<devices.size();i++){
            String deviceName=devices.get(i).get("device_name").toString();
            if (redisData2.get(deviceName)!=null){
                devices.get(i).putAll(redisData2.get(deviceName));
            }
            //处理di
            if (devices.get(i).get("di")!=null){
                Map<String, Object> diMap=(Map<String, Object>) devices.get(i).get("di");
                int di=Integer.parseInt(diMap.get("val").toString().substring(0,diMap.get("val").toString().indexOf(".")));
                devices.get(i).put("di",di);
            }else{
                devices.get(i).put("di",0); //没有开关标签 默认0
            }
            devicesMap.put(deviceName,devices.get(i));
        }
        //设备排序
        Map<String, Object> devicesSort=cacheService.getSortData(getMeterParams);
        //排序
        List<Map<String, Object>> resultDevice =new ArrayList<>();
        List<String> deviceList=Arrays.asList(devicesSort.get("sort_device_names").toString().split(","));

        Map<String,List<Map<String, Object>>> titleMap=new LinkedHashMap<>();
        for (int i=0;i<deviceList.size();i++){
            if (devicesMap.get(deviceList.get(i))!=null){
//                resultDevice.add(devicesMap.get(deviceList.get(i)));
                String panelName=devicesMap.get(deviceList.get(i)).get("panelName").toString();
                List<Map<String, Object>> dscList=new ArrayList<>();
                if (titleMap.get(panelName)!=null){
                    dscList=titleMap.get(panelName);
                }
                dscList.add(devicesMap.get(deviceList.get(i)));
                titleMap.put(panelName,dscList);
            }
        }
        //添加is_title
        for (Map.Entry<String,List<Map<String, Object>>> entry:titleMap.entrySet()){
            String is_title= entry.getKey();
            List<Map<String, Object>> dList= entry.getValue();
            int index=dList.size()/2;
            dList.get(index).put("is_title",is_title);
            resultDevice.addAll(dList);
        }



        Map<String, Object> result=new HashMap<>(3);
        result.put("device",resultDevice);
        result.put("num_per_line",devicesSort.get("num_per_line"));
        result.put("route_type",devicesSort.get("route_type"));
        return WebResult.success(result);
    }

    /**
     * 获取选择框数据
     * @param map
     * @return
     */
    @Override
    public WebResult getSelectsData(Map<String, Object> map) {
        List<Map<String, Object>> factorys =commonService.getFactoryByPid(map);
        List<Map<String, Object>> transformerroom = powerDao.getTransformerroomByPid(map);
        List<Map<String, Object>> devices = powerDao.getSelectLine(map);

        Map<String,Object> result=new LinkedHashMap<>(3);
        result.put("factorys", factorys);
        result.put("transformerrooms", transformerroom);
        result.put("devices", devices);
        return WebResult.success(result);
    }


}
