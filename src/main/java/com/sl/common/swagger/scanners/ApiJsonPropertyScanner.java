package com.sl.common.swagger.scanners;

import com.sl.common.swagger.config.ApiJsonPluginManager;
import com.sl.common.swagger.contexts.ApiJsonDocumentationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiJsonPropertyScanner {

    private final ApiJsonPluginManager apiJsonPluginManager;

    @Autowired
    public ApiJsonPropertyScanner(ApiJsonPluginManager apiJsonPluginManager){
        this.apiJsonPluginManager = apiJsonPluginManager;
    }

    public ApiJsonPluginManager getApiJsonPluginManager() {
        return apiJsonPluginManager;
    }

    public void read(ApiJsonDocumentationContext context){
        apiJsonPluginManager.apiJsonProperty(context);
    }
}
