package com.sl.common.swagger.scanners;

import com.sl.common.swagger.config.ApiJsonPluginManager;
import com.sl.common.swagger.contexts.ApiJsonRefContext;
import org.springframework.stereotype.Component;

@Component
public class ApiJsonRefModelReplacer {

    private final ApiJsonPluginManager apiJsonPluginManager;

    public ApiJsonRefModelReplacer(ApiJsonPluginManager apiJsonPluginManager){
        this.apiJsonPluginManager = apiJsonPluginManager;
    }

    public void replace (ApiJsonRefContext apiJsonRefContext){
        this.apiJsonPluginManager.apiJsonReplace(apiJsonRefContext);
    }
}
