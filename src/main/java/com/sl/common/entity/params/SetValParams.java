package com.sl.common.entity.params;

import com.sl.common.entity.OperationLogs;

/**
 * Created by IntelliJ IDEA.
 * Author: 李旭日
 * Date: 2020/10/9 11:49
 * FileName: SetValParams
 * Description: 下置公共方法传参实体类
 */
public class SetValParams {
    private int projectId;
    private int factoryId;
    //通讯机名称
    private String tgName;
    //通讯机id
    private int tgId;
    //下置标签  eg: a1_b1_di
    private String tag;
    //下置的具体值  eg:0
    private String val;
    //造作记录实体类
    private OperationLogs operationLogs;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(int factoryId) {
        this.factoryId = factoryId;
    }

    public String getTgName() {
        return tgName;
    }

    public void setTgName(String tgName) {
        this.tgName = tgName;
    }

    public int getTgId() {
        return tgId;
    }

    public void setTgId(int tgId) {
        this.tgId = tgId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public OperationLogs getOperationLogs() {
        return operationLogs;
    }

    public void setOperationLogs(OperationLogs operationLogs) {
        this.operationLogs = operationLogs;
    }
}
