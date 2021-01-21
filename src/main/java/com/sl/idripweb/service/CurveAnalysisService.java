package com.sl.idripweb.service;

import com.sl.common.utils.WebResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface CurveAnalysisService {

    //获取配电室/水泵房/调压站下meter列表
    public WebResult getRoomDevice(Map<String,Object> map);

    //获取配电室低压进线表
    public WebResult getLowIncomingLine(Map<String,Object> map);

    //页面数据
    public WebResult index(HttpServletRequest request, Map<String,Object> map);

}
