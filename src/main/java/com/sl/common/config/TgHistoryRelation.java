package com.sl.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
@Configuration
@PropertySource(value = {"classpath:tgHistoryRelation.properties"}, encoding = "utf-8")
@ConfigurationProperties(prefix = "tag")
public class TgHistoryRelation {

    private Map<String, String> elec;

    private Map<String, String> fire;

    private Map<String, String> photovoltaic;

    public Map<String, String> getElec() {
        return elec;
    }

    public void setElec(Map<String, String> elec) {
        this.elec = elec;
    }

    public Map<String, String> getFire() {
        return fire;
    }

    public void setFire(Map<String, String> fire) {
        this.fire = fire;
    }

    public Map<String, String> getPhotovoltaic() {
        return photovoltaic;
    }

    public void setPhotovoltaic(Map<String, String> photovoltaic) {
        this.photovoltaic = photovoltaic;
    }
}
