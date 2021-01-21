package com.sl.idripweb.service.impl;
import com.sl.common.utils.DateUtil;
import com.sl.common.utils.SetParams;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.dao.AlermManageDao;
import com.sl.idripweb.service.AlermManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@SuppressWarnings("unused")
@Service("alermManagerService")
/**
 * 报警管理impl
 * @author lxr
 */
public class AlermManageServiceImpl implements AlermManagerService {
    @Autowired
    private AlermManageDao alermManagerDao;

    /**
     * 获取初始化数据
     * @param map
     * @return
     */
    @Override
    public WebResult initData(Map<String, Object> map) {
        //分页  默认20页
        SetParams.setPagParam(map);
        //开始时间=三个月之前  结束时间=当前时间
        map.put("btime",DateUtil.parseDateToStr(DateUtil.addDate(new Date(),0,-3,0,0,0,0,0),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS) );
        map.put("etime",DateUtil.parseDateToStr(new Date(),DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        //返回数据+数据条数
        LinkedHashMap<String,Object> result=new LinkedHashMap<>(2);
        result.put("total", alermManagerDao.getAlermDataCount(map));
        result.put("alermData", alermManagerDao.getAlermData(map));
        return WebResult.success(result);
    }

    /**
     * 查询数据
     * @param map
     * @return
     */
    @Override
    public WebResult getData(Map<String, Object> map) {
        //分页  默认20页
        SetParams.setPagParam(map);
        //返回数据+数据条数
        if (map.get("etime")!=null&&!map.get("etime").toString().equals("")){
            map.put("etime",map.get("etime")+" 23:59:59");
        }
        LinkedHashMap<String,Object> result=new LinkedHashMap<>(2);
        result.put("total", alermManagerDao.getAlermDataCount(map));
        result.put("alermData", alermManagerDao.getAlermData(map));
        return WebResult.success(result);
    }

    /**
     *获取下拉框数据 分类+厂区等
     * @param map
     * @return
     */
    @Override
    public WebResult getCategoryAndRoom(Map<String, Object> map) {
        ArrayList<Map<String, Object>> category = alermManagerDao.getAlermCategoryByPeoject(map);
        ArrayList<Map<String, Object>> type = alermManagerDao.getAlermCategoryN3();
        ArrayList<Map<String, Object>> allBusinessName = alermManagerDao.getAllBusinessName(map);
        ArrayList<Map<String, Object>> factory = alermManagerDao.getFactoryByProjectid(map);

        LinkedHashMap<String,Object> result=new LinkedHashMap<>(4);
        result.put("category", category);
        result.put("type", type);
        result.put("businessNameList", allBusinessName);
        result.put("factory", factory);
        return WebResult.success(result);
    }

    /**
     *报警处理详情
     * @param map
     * @return
     */
    @Override
    public WebResult queryDealDetail(Map<String, Object> map) {
        if (map.get("logid")==null){
            return WebResult.error(301);
        }
        Map<String, Object> queryDealDetail = alermManagerDao.queryDealDetail(String.valueOf(map.get("logid")));
        return WebResult.success(queryDealDetail);
    }
    /**
     *报警处理
     * @param map
     * @return
     */
    @Override
    @Transactional
    public WebResult setDeal(Map<String, Object> map) {
        if (map.get("logid")==null||map.get("msg")==null){
            return WebResult.error(301);
        }
        String lid =map.get("logid").toString();
        //处理
        Map<String, Object> queryDealDetail =alermManagerDao.queryDealDetail(lid);
        if (queryDealDetail == null) {
            alermManagerDao.saveDeal(map);
        }else{
            alermManagerDao.updateDeal(map);
        }

        //处理后插入历史表
        Map<String,Object> params=new HashMap<>();
        params.put("lid",lid);
        Map<String, Object> alerm=alermManagerDao.selectAlermById(params); //alerm信息
        alerm.put("operater",map.get("user_name"));
        if(alerm.get("timely_time")==null||"".equals(String.valueOf(alerm.get("timely_time")))){  //沒有录入信息
                alerm.put("is_timely",1);  //默认是1 及时处理
        }else {
            Date log_time = DateUtil.parseStrToDate(String.valueOf(alerm.get("log_time")), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS);
            int timely_time = Integer.parseInt(String.valueOf(alerm.get("timely_time")));
            Date date = DateUtil.addDate(log_time, 0, 0, 0, timely_time, 0, 0, 0);
            if (DateUtil.isEffectiveDate(new Date(), log_time, date, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS)) {
                alerm.put("is_timely", 1);  //及时处理
            } else {
                alerm.put("is_timely", 0);  //不是及时处理
            }
        }
        List<Map> list=new ArrayList<>();
        list.add(alerm);
        int i = alermManagerDao.insertAllHistory(list);  //插入历史数据
        int d = alermManagerDao.deleteLog(lid);  //删除报警表

        return WebResult.success();
    }

    /**
     * 维修登记
     * @param map
     * @return
     */
    @Override
    public WebResult setRepairMsg(Map<String, Object> map) {
        if (map.get("logid")==null||map.get("msg")==null){
            return WebResult.error(301);
        }
        //先查询String logid , String msg
        String logid = map.get("logid").toString();
        Map<String, Object> queryDealDetail =alermManagerDao.queryDealDetail(logid);
        if (queryDealDetail == null) {
             alermManagerDao.saveRepairMsg(map);
        } else {
             alermManagerDao.updateRepairMsg(map);
        }
        alermManagerDao.updateIsRegister(logid);
        alermManagerDao.updateIsRegisterHistory(logid);
        return WebResult.success();
    }
    /**
     * 全局报警
     * @param map
     * @return
     */
    @Override
    public WebResult getPopupData(Map<String, Object> map) {
        //分页  默认20页
        SetParams.setPagParam(map);

        //返回数据+数据条数
        LinkedHashMap<String,Object> result=new LinkedHashMap<>(2);
        result.put("totalCount", alermManagerDao.getPopupDataCount(map));
        result.put("data", alermManagerDao.getPopupData(map));
        return WebResult.success(result);
    }


    /**
     * 全局报警确认
     * @param map
     * @return
     */
    @Override
    public WebResult confirm(Map<String, Object> map) {
        if (map.get("ids")==null){
            return WebResult.error(301);
        }
        String[] split = String.valueOf(map.get("ids")).split(",");
        List<String> list = Arrays.asList(split);
        map.put("list", list);
        alermManagerDao.confirm(map);
        return WebResult.success();
    }


    /**
     * 报警打印
     * @param map
     * @return
     */
    @Override
    public WebResult printAlerm(Map<String, Object> map) {
        if (map.get("etime")!=null&&!map.get("etime").toString().equals("")){
            map.put("etime",map.get("etime")+" 23:59:59");
        }
        ArrayList<Map> alermList=alermManagerDao.printAlerm(map);
        Map<String, Object> time=new HashMap<>();
        int count=alermList.size();
        if (count>0){
            time.put("btime",alermList.get(0).get("log_time").toString());
            time.put("etime",alermList.get(count-1).get("log_time").toString());
        }

        Map<String, Object> factoryAndCate=new HashMap<>();
        ArrayList<Map<String, Object>> cate = alermManagerDao.getAlermCateGoryByid(map);
        ArrayList<Map<String, Object>> factory = alermManagerDao.getFactoryByid(map);
        factoryAndCate.put("caet", cate);
        factoryAndCate.put("factory", factory);

        LinkedHashMap<String,Object> result=new LinkedHashMap<>(3);
        result.put("data", alermList);
        result.put("time", time);
        result.put("factoryAndCate", factoryAndCate);
        return WebResult.success(result);
    }
    /**
     * 报警导出数据
     * @param map
     * @return
     */
    @Override
    public ArrayList<Map> exportData(Map<String, Object> map) {
       return alermManagerDao.printAlerm(map);
    }


}
