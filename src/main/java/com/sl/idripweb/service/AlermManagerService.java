package com.sl.idripweb.service;

import com.sl.common.utils.WebResult;

import java.util.ArrayList;
import java.util.Map;

public interface AlermManagerService {

    //初始化报警数据
    public WebResult initData(Map<String, Object> map);

    //获取报警数据
    public WebResult getData(Map<String, Object> map);

    //获取下拉框数据 分类+厂区等
    public WebResult getCategoryAndRoom(Map<String, Object> map);

    //报警处理详情
    public WebResult queryDealDetail(Map<String, Object> map);

    //报警处理
    public WebResult setDeal(Map<String, Object> map);

    //维修登记
    public WebResult setRepairMsg(Map<String, Object> map);

    //全局报警
    public WebResult getPopupData(Map<String, Object> map);

    //全局报警确认
    public WebResult confirm(Map<String, Object> map);

    //报警打印
    public WebResult printAlerm(Map<String, Object> map);

    //导出报警数据
    public ArrayList<Map> exportData(Map<String, Object> map);
}
