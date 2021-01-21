package com.sl.common.dao;

import com.sl.common.entity.CategoryTree;
import com.sl.common.entity.OperationLogs;
import com.sl.common.entity.params.SetValParams;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface CommonDao {

    //获取所有变压器
    ArrayList<Map<String, Object>> getTransformerList(Map<String, Object> map);
    //获取树
    List<CategoryTree> getCategoryTree(Map<String, Object> map);

    //获取rbac_menu数据
    List<Map<String, Object>> getMenuList(Map<String, Object> map);
    //验证操作密码
    Map<String, Object> verifyPWD(Map<String, Object> map);

    Integer getDeviceIdByName(Map<String, Object> map);




    //根据项目id  获取所有厂区列表
    ArrayList<Map<String, Object>> getFactoryByProjectid(Map<String, Object> map);



    //下至操作！  获取通讯机
    ArrayList<Map<String, Object>> getTGInfoByName(SetValParams setValParams);
    //写日志
    int setOperationLogs(OperationLogs operationLogsVo);


}
