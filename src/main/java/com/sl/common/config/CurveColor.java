package com.sl.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@Configuration
@PropertySource(value = {"classpath:curveColor.properties"}, encoding = "utf-8")
@ConfigurationProperties(prefix = "curve")
public class CurveColor {
    private Map<String,String> color;

    public Map<String, String> getColor() {
        return color;
    }

    public void setColor(Map<String, String> color) {
        this.color = color;
    }
}
