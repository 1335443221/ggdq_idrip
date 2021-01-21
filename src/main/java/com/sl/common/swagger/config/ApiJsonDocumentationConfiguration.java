package com.sl.common.swagger.config;

import com.sl.common.swagger.plugins.ApiJsonObjectPlugin;
import com.sl.common.swagger.plugins.ApiJsonPropertyPlugin;
import com.sl.common.swagger.plugins.ApiJsonRefModelReplacePlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.config.EnablePluginRegistries;

@Configuration
@ComponentScan(basePackages = {
        "com.sl.common.swagger.plugins",
        "com.sl.common.swagger.config",
        "com.sl.common.swagger.scanners"
})
@EnablePluginRegistries({
        ApiJsonObjectPlugin.class,
        ApiJsonPropertyPlugin.class,
        ApiJsonRefModelReplacePlugin.class
})
public class ApiJsonDocumentationConfiguration {

    @Bean
    public ApiJsonClassLoader apiJsonClassLoader(){
        return new ApiJsonClassLoader();
    }
}
