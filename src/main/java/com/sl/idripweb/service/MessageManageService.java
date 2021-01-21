package com.sl.idripweb.service;

import com.sl.common.utils.WebResult;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface MessageManageService {

    public WebResult getMessageInfo(Map<String, Object> map);
    public WebResult getMessageCount(Map<String, Object> map);
    public WebResult save(Map<String, Object> map);
    public WebResult update(Map<String, Object> map);
    public WebResult delete(Map<String, Object> map);
    public WebResult query(Map<String, Object> map);
    public WebResult getRoles(Map<String, Object> map);
    public WebResult getUserByRoleId(Map<String, Object> map);
}
