package com.sl.idripweb.service;

import com.sl.common.utils.WebResult;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface PowerService {

    public WebResult getDatav3(Map<String, Object> map);
    public WebResult getDeviceMonitor(Map<String, Object> map);
    public WebResult getSelectsData(Map<String, Object> map);

}
