package com.sl.idripweb.service;

import com.sl.common.utils.WebResult;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ElecAnalysisService {

    public WebResult index(Map<String, Object> map);
    public WebResult getLoadRate(Map<String, Object> map);

}
