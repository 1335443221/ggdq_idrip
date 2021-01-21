package com.sl.common.config;

import com.sl.common.utils.RedisUtil;
import com.sl.common.utils.SpringBeanUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Date;

//session 监听类
public class HttpSessionListeners implements HttpSessionListener {

    private Logger logger = Logger.getLogger(HttpSessionListeners.class);

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        logger.info("创建Session=====" + session.getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
//        String key = "ove_session:" + session.getId();
//        RedisUtil redisUtil = SpringBeanUtil.getBean(RedisUtil.class);
//        redisUtil.del(4, key);
        logger.info("销毁Session=====" + session.getId());
        logger.info("Session创建时间=====" + new Date(session.getCreationTime()));
    }
}
