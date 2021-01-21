package com.sl.idripweb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sl.common.config.ConstantConfig;
import com.sl.common.service.CacheService;
import com.sl.common.service.CommonService;
import com.sl.common.utils.DateUtil;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.dao.ParkOverview3DDao;
import com.sl.idripweb.service.ParkOverview3DService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("parkOverview3DServiceImpl")
public class ParkOverview3DServiceImpl implements ParkOverview3DService {

    @Autowired
    private ParkOverview3DDao parkOverview3DDao;
    @Autowired
    private ConstantConfig constantConfig;
    @Autowired
    private CommonService commonService;
    @Autowired
    private CacheService cacheService;

    /**
     * 3D配电室数据
     * @param map
     * @return
     */
    @Override
    public WebResult transformerroomData(Map<String, Object> map) {
        if (map.get("room_id")==null||map.get("room_id").toString().equals("")){
            return WebResult.error(301);
        }
        //日期 当天日期
        map.put("date", DateUtil.parseDateToStr(new Date(),DateUtil.DATE_FORMAT_YYYY_MM_DD));
        //
        double power=parkOverview3DDao.transformerroomEnergyData(map);


        List<Map<String, Object>>  temperatureControllerList=parkOverview3DDao.getTemperatureController(map);
        List<Map<String, Object>> tagList =new ArrayList<>();
        Map<String, Object> tagMap= new HashMap<>();
        tagMap.put("tag_name","t");  //温度
        tagList.add(tagMap);
        Map<String, Object> tagMap2= new HashMap<>();
        tagMap2.put("tag_name","sd");  //湿度
        tagList.add(tagMap2);
        Map<String, Object> redisData= commonService.getRedisData(temperatureControllerList,tagList);

        List<Map<String, Object>>  meterList=new ArrayList<>();
        for (Map<String, Object> meter:temperatureControllerList){
            Map<String, Object> meterMap=new HashMap<>();
            meterMap.put("desc",meter.get("desc"));
//            String key=meter.get("tg_id")+meter.get("device_name");
            meterMap.put("t",redisData.get(meter.get("device_name")+"_t")==null?"0.0": JSONObject.parseObject(JSON.toJSONString(redisData.get(meter.get("device_name")+"_t"))).get("val"));
            meterMap.put("sd",redisData.get(meter.get("device_name")+"_sd")==null?"0.0":JSONObject.parseObject(JSON.toJSONString(redisData.get(meter.get("device_name")+"_sd"))).get("val"));
            meterList.add(meterMap);
        }
        Map<String, Object> result=new HashMap<>(2);
        //总能耗
        result.put("power",(double)Math.round(power * 100) / 100);
        result.put("meterList",meterList);
        //温度列表
        return WebResult.success(result);
    }



}
