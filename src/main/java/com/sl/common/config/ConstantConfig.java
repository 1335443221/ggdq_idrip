package com.sl.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Created by IntelliJ IDEA.
 * Author: 李旭日
 * Date: 2020/9/30 15:48
 * FileName: ConstantConfig
 * Description: 配置文件
 */
@Validated
@ConfigurationProperties(prefix = "constant")
@Service
@Data
public class ConstantConfig {
    public  String  environment;
    public  String  oveSession;
    public  String  xuansiIp;
    public String elecMeter;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getOveSession() {
        return oveSession;
    }

    public void setOveSession(String oveSession) {
        this.oveSession = oveSession;
    }

    public String getXuansiIp() {
        return xuansiIp;
    }

    public void setXuansiIp(String xuansiIp) {
        this.xuansiIp = xuansiIp;
    }

    public String getElecMeter() {
        return elecMeter;
    }

    public void setElecMeter(String elecMeter) {
        this.elecMeter = elecMeter;
    }
}
