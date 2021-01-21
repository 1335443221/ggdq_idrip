package com.sl.idripweb.service;

import com.sl.common.utils.WebResult;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ParkOverview3DService {

    public WebResult transformerroomData(Map<String, Object> map);

}
