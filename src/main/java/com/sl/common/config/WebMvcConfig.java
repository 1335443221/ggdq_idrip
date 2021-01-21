package com.sl.common.config;

import com.sl.common.interceptor.WebControllerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by IntelliJ IDEA.
 * Author: 李旭日
 * Date: 2020/9/30 15:48
 * FileName: WebConfig
 * Description: 拦截器文件配置
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Bean
	public HandlerInterceptor getWebControllerInterceptor(){
		return new WebControllerInterceptor();
	}

	/**
	 *  //* 配置WEB拦截器，阻止普通用户和游客进入管理员界面，阻止游客进行个人信息有关操作
	 *  并且把project_id等项目基础数据存进去
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getWebControllerInterceptor())
				.addPathPatterns("/*/**")
				//添加swagger静态资源过滤
				.excludePathPatterns("/login/login", "/login/logout", "/login/loginpage", "/swagger-resources/**", "/swagger-ui.html/**", "/druid/**");
	}

}
