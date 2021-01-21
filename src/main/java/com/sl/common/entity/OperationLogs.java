package com.sl.common.entity;

public class OperationLogs {
    private int id;
    private int uid;
    private int project_id;
    private String ip;
    private String client;
    private String request_uri;
    private int device_type_relation_id;
    private String behavior_type;
    private String detailed_type;
    private String behavior_result;
    private String log_time;

    @Override
    public String toString() {
        return "operationLogs{" +
                "id=" + id +
                ", uid=" + uid +
                ", project_id=" + project_id +
                ", ip='" + ip + '\'' +
                ", client=" + client +
                ", request_uri='" + request_uri + '\'' +
                ", device_type_relation_id=" + device_type_relation_id +
                ", behavior_type='" + behavior_type + '\'' +
                ", detailed_type='" + detailed_type + '\'' +
                ", behavior_result='" + behavior_result + '\'' +
                ", log_time='" + log_time + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getRequest_uri() {
        return request_uri;
    }

    public void setRequest_uri(String request_uri) {
        this.request_uri = request_uri;
    }

    public int getDevice_type_relation_id() {
        return device_type_relation_id;
    }

    public void setDevice_type_relation_id(int device_type_relation_id) {
        this.device_type_relation_id = device_type_relation_id;
    }

    public String getBehavior_type() {
        return behavior_type;
    }

    public void setBehavior_type(String behavior_type) {
        this.behavior_type = behavior_type;
    }

    public String getDetailed_type() {
        return detailed_type;
    }

    public void setDetailed_type(String detailed_type) {
        this.detailed_type = detailed_type;
    }

    public String getBehavior_result() {
        return behavior_result;
    }

    public void setBehavior_result(String behavior_result) {
        this.behavior_result = behavior_result;
    }

    public String getLog_time() {
        return log_time;
    }

    public void setLog_time(String log_time) {
        this.log_time = log_time;
    }
}
