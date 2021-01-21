package com.sl.idripweb.service;

import com.sl.common.utils.WebResult;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AlermConfigService {

    public WebResult query(Map<String, Object> map);
    public WebResult getCategoryRelation(Map<String, Object> map);
    public WebResult getRoomDeviceTagTree(Map<String, Object> map);
    public WebResult getAlermType(Map<String, Object> map);
    public WebResult getTagByDeviceId(Map<String, Object> map);
    public WebResult save(Map<String, Object> map);
    public WebResult update(Map<String, Object> map);
    public WebResult delete(Map<String, Object> map);


}
