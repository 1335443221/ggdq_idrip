package com.sl.common.config;

import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionListener;
import java.util.List;

@Component
public class HttpSessionListenersConfig extends RedisHttpSessionConfiguration {

    @Override
    public void setHttpSessionListeners(List<HttpSessionListener> listeners) {
        super.setHttpSessionListeners(listeners);
    }

}
