package com.sl.idripweb.service.impl;

import com.sl.common.config.ConstantConfig;
import com.sl.common.service.CacheService;
import com.sl.common.service.CommonService;
import com.sl.common.utils.SetParams;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.dao.MessageManageDao;
import com.sl.idripweb.service.MessageManageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("messageManageServiceImpl")
public class MessageManageServiceImpl implements MessageManageService {

    @Autowired
    private MessageManageDao messageManageDao;
    @Autowired
    private ConstantConfig constantConfig;
    @Autowired
    private CommonService commonService;
    @Autowired
    private CacheService cacheService;

    /**
     * 查看按钮
     * @param map
     * @return
     */
    @Override
    public WebResult getMessageInfo(Map<String, Object> map) {
        if (map.get("date")==null){
            return  WebResult.error(301);
        }
        //分页  默认20页
        SetParams.setPagParam(map);
        //开始时间  结束时间=开始时间后一天
        map.put("btime",map.get("date")+" 00:00:00");
        map.put("etime",map.get("date")+" 23:59:59");
        List<Map<String,Object>> messageInfo=messageManageDao.getMessageInfo(map);
        int total=messageManageDao.getMessageInfoCount(map);

        //获取短信错误信息映射
        List<Map<String, Object>> messageErrorcodeInfo = messageManageDao.getMessageErrorcodeInfo(map);
        Map<String, Object> errCodeMap = new HashMap<>();
        messageErrorcodeInfo.stream().forEach(each -> {
            errCodeMap.put(String.valueOf(each.get("err_info")), each.get("operational_suggestions"));
        });
        Map<String, Object> result=new HashMap<>();
        Map<String, Object> dataList = new HashMap<>();
        messageInfo.stream().forEach(each -> {
            String sendstatus = String.valueOf(each.get("sendstatus"));
            if("3".equals(sendstatus)){
                each.put("errcode", "");
                each.put("sendstatus", "发送成功");
            }else if("2".equals(sendstatus)){
                each.put("errcode", errCodeMap.get(sendstatus) != null ? errCodeMap.get(sendstatus) : "");
                each.put("sendstatus", "发送失败");
            }else if("1".equals(sendstatus)){
                each.put("errcode", errCodeMap.get(sendstatus) != null ? errCodeMap.get(sendstatus) : "");
                each.put("sendstatus", "等待回执");
            }
        });
        dataList.put("MessageInfoData", messageInfo);
        dataList.put("total", total);
        result.put("data_list",dataList);
        return WebResult.success(result);
    }
    /**
     * 统计
     * @param map
     * @return
     */
    @Override
    public WebResult getMessageCount(Map<String, Object> map) {
        //分页  默认20页
        SetParams.setPagParam(map);
        if (map.get("edate")!=null&&!"".equals(String.valueOf(map.get("edate")))) {
            //结束日期不为空
            map.put("edate", map.get("edate")+" 23:59:59");
        }
        if (map.get("bdate")!=null&&!"".equals(String.valueOf(map.get("bdate")))) {
            //结束日期不为空
            map.put("bdate", map.get("bdate")+" 00:00:00");
        }

        List<Map<String,Object>> messageCount=messageManageDao.getMessageCount(map);
        int total=messageManageDao.getMessageCountCount(map);

        //获取账户信息
        List<Map<String, Object>> accountInfo = messageManageDao.getAccountInfo(map);

        Map<String, Object> result=new HashMap<>();
        Map<String, Object> dataList = new HashMap<>();
        dataList.put("MessageCountData", messageCount);
        dataList.put("total", total);
        result.put("data_list",dataList);
        if(accountInfo.size() > 0){
            result.put("account_list",accountInfo.get(0));
        }
        return WebResult.success(result);
    }

    /**
     * 新增
     * @param map
     * @return
     */
    @Override
    public WebResult save(Map<String, Object> map) {
        if (map.get("role_id")==null||map.get("alerm_level")==null){
            return  WebResult.error(301);
        }
        // 插入成功存入$success_id
        List<String> successId = new ArrayList<>();
        // 插入失败存入$fail_id
        List<String> failId = new ArrayList<>();
        List<String> roleIds = Arrays.asList(String.valueOf(map.get("role_id")).split(","));
        for(String roleId : roleIds){
            map.put("role_id", roleId);
            int checkSame = messageManageDao.queryCount(map);
            if(checkSame <= 0){// 没有重复插入，则插入
                int saveResult = messageManageDao.save(map);
                if(saveResult == 1){
                    successId.add(roleId);
                }else{
                    failId.add(roleId);
                }
            }else{// 重复插入
                failId.add(roleId);
            }
        }
        if(failId.size() > 0){
            return WebResult.error(501, "角色同等级的配置已存在,请重新配置", StringUtils.join(failId, ","));
        }else{
            return WebResult.success();
        }
    }
    /**
     * 修改
     * @param map
     * @return
     */
    @Override
    public WebResult update(Map<String, Object> map) {
        if (map.get("role_id")==null||map.get("alerm_level")==null||map.get("id")==null){
            return  WebResult.error(301);
        }
        // 查重
        map.put("is_edit", 1);
        int checkSame = messageManageDao.queryCount(map);
        if(checkSame > 0){
            return WebResult.error(501, "相同等级相同角色的配置已存在");
        }else{
            int result=messageManageDao.update(map);
            if (result>0){
                return  WebResult.success();
            }else{
                return  WebResult.error(201);
            }
        }
    }

    /**
     * 删除
     * @param map
     * @return
     */
    @Override
    public WebResult delete(Map<String, Object> map) {
        if (map.get("id")==null){
            return  WebResult.error(301);
        }
        int result=messageManageDao.delete(map);
        if (result>0){
            return  WebResult.success();
        }else{
            return  WebResult.error(201);
        }
    }

    /**
     * 查看
     * @param map
     * @return
     */
    @Override
    public WebResult query(Map<String, Object> map) {
        SetParams.setPagParam(map);
        List<Map<String, Object>> manage = messageManageDao.query(map);
        HashMap<String, Object> result = new HashMap<>();
        int totalCount = messageManageDao.queryCount(map);
        Map<String, Object> dataList = new HashMap<>();
        dataList.put("configData", manage);
        dataList.put("total", totalCount);
        result.put("data_list", dataList);
        return  WebResult.success(result);
    }
    /**
     * 获取角色列表（岗位）
     * @param map
     * @return
     */
    @Override
    public WebResult getRoles(Map<String, Object> map) {
        List<Map<String, Object>> roles = messageManageDao.getRoles(map);
        HashMap<String, Object> result = new LinkedHashMap<>(1);
        result.put("data_list", roles);
        return  WebResult.success(result);
    }

    /**
     * 通过角色id获取user信息
     * @param map
     * @return
     */
    @Override
    public WebResult getUserByRoleId(Map<String, Object> map) {
        if(map.get("role_id") == null) return WebResult.error(301);
        map.put("role_id", String.valueOf(map.get("role_id")).split(","));
        List<Map<String, Object>> users = messageManageDao.getUserByRoleId(map);
        if(users != null && users.size() > 0){
            users.forEach(each -> {
                String roleName = String.valueOf(each.get("role_name"));
                each.put("role_name", roleName.substring(roleName.indexOf("-")+1));
            });
            return WebResult.success(users);
        }else{
            return WebResult.error(207);
        }
    }

}
