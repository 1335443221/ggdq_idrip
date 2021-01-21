package com.sl.idripweb.service;

import com.sl.common.utils.WebResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface SynthesizeEnergyListService {

    //能耗分析==>综合能耗列表
    public WebResult index(HttpServletRequest request, Map<String,Object> map);

}
