package com.sl.common.service;

import com.sl.common.config.ConstantConfig;
import com.sl.common.dao.CacheDao;
import com.sl.common.entity.params.GetMeterParams;
import com.sl.idripweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author: 李旭日
 * Date: 2020/9/30 15:48
 * FileName: CacheService
 * Description: 缓存类
 */
@Service
public class CacheService {
    @Autowired
    private UserService userService;
    @Autowired
    ConstantConfig constant;
    @Autowired
    CacheDao cacheDao;

    /**
     * 从session中获取用户信息
     * @param sessionID
     * @return
     */
    @Cacheable("sessionUser")
    public Map<String, Object> getUserCache(String sessionID) {
//        return  userService.getUserInfo(sessionID);
        return null;
    }

    /**
     * 获取设备
     * @param getMeterParams
     * @return
     */
    @Cacheable("getMeterInfo")
    public List<Map<String, Object>> getMeterInfo(GetMeterParams getMeterParams) {
        List<Map<String, Object>> meter = new ArrayList<>();
         if (constant.getElecMeter().equals(getMeterParams.getDeviceType())) {
            meter = cacheDao.getElecMeter(getMeterParams);
        }
        return meter;
    }
    /**
     * 获取设备
     * @param getMeterParams
     * @return
     */
    @Cacheable("getSortData")
    public Map<String, Object> getSortData(GetMeterParams getMeterParams) {
        Map<String, Object> meter = new HashMap<>();
         if (constant.getElecMeter().equals(getMeterParams.getDeviceType())) {
            meter = cacheDao.getSortData(getMeterParams);
        }
        return meter;
    }

    /**
     * 获取标签数据
     * @param device_type 设备类型
     * @return
     */
    @Cacheable("getTag")
    public List<Map<String, Object>> getTag(String device_type) {
        List<Map<String, Object>> tagMap = new ArrayList<>();
        if (constant.getElecMeter().equals(device_type)) {   //类型是电表  返回电表标签
            tagMap = cacheDao.getElecTag();
        }
        return tagMap;
    }


}
