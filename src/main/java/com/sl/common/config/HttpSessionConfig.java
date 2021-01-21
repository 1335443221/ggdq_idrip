package com.sl.common.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.SessionEventHttpSessionListenerAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400, redisNamespace = "ove_session")
public class HttpSessionConfig {

    @Autowired
    private CookieConfig cookieConfig;

    @Bean
    public CookieHttpSessionStrategy cookieHttpSessionStrategy(){
        CookieHttpSessionStrategy strategy=new CookieHttpSessionStrategy();
        DefaultCookieSerializer cookieSerializer=new DefaultCookieSerializer();
        //清除目前的ove_session
        Cookie newCookie=new Cookie("ove_session", null); //假如要删除名称为ove_session的Cookie
        newCookie.setMaxAge(0); //立即删除型

        cookieSerializer.setCookieName(cookieConfig.getName());//cookies名称
        cookieSerializer.setCookiePath(cookieConfig.getPath());
        cookieSerializer.setDomainName(cookieConfig.getDomainName());
        cookieSerializer.setUseHttpOnlyCookie(cookieConfig.getUseHttpOnly());
        strategy.setCookieSerializer(cookieSerializer);
        return strategy;
    }

    @Bean
    public SessionEventHttpSessionListenerAdapter sessionEventHttpSessionListenerAdapter(){
        List<HttpSessionListener> list = new ArrayList<>();
        list.add(new HttpSessionListeners());
        return new SessionEventHttpSessionListenerAdapter(list);
    }

    @Bean
    @Qualifier("springSessionDefaultRedisSerializer")
    public RedisSerializer<Object> setDefaultRedisSerializer(){
        return new GenericFastJsonRedisSerializer();
    }

}
