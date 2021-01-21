package com.sl.common.entity.params;

import com.sl.common.entity.OperationLogs;

/**
 * Created by IntelliJ IDEA.
 * Author: 李旭日
 * Date: 2020/10/9 11:49
 * FileName: GetMeterParams
 * Description: 获取设备传参实体类
 */
public class GetMeterParams {
    private String deviceType;
    private String projectId;
    private String factoryId;
    private String waterPumpRoomId;
    private String transformerroomId;
    private String categoryTypeId;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getWaterPumpRoomId() {
        return waterPumpRoomId;
    }

    public void setWaterPumpRoomId(String waterPumpRoomId) {
        this.waterPumpRoomId = waterPumpRoomId;
    }

    public String getTransformerroomId() {
        return transformerroomId;
    }

    public void setTransformerroomId(String transformerroomId) {
        this.transformerroomId = transformerroomId;
    }

    public String getCategoryTypeId() {
        return categoryTypeId;
    }

    public void setCategoryTypeId(String categoryTypeId) {
        this.categoryTypeId = categoryTypeId;
    }
}
