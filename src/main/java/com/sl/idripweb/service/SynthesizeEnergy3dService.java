package com.sl.idripweb.service;

import com.sl.common.utils.WebResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface SynthesizeEnergy3dService {

    //能耗分析==>综合能耗地图首页
    public WebResult index(HttpServletRequest request);

}
