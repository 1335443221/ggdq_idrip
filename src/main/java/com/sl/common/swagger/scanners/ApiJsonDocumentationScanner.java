package com.sl.common.swagger.scanners;

import com.sl.common.swagger.contexts.ApiJsonDocumentationContext;
import com.sl.common.swagger.documentation.ApiJsonDocumentation;
import com.sl.common.swagger.documentation.ApiJsonDocumentationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiJsonDocumentationScanner {

    private ApiJsonObjectScanner apiJsonObjectScanner;
    private ApiJsonPropertyScanner apiJsonPropertyScanner;

    @Autowired
    public ApiJsonDocumentationScanner(ApiJsonObjectScanner apiJsonObjectScanner, ApiJsonPropertyScanner apiJsonPropertyScanner){
        this.apiJsonObjectScanner = apiJsonObjectScanner;
        this.apiJsonPropertyScanner = apiJsonPropertyScanner;
    }

    public ApiJsonDocumentation scan(ApiJsonDocumentationContext context){
        
        apiJsonObjectScanner.read(context);
        apiJsonPropertyScanner.read(context);

        return new ApiJsonDocumentationBuilder().name(context.getName()).properties(context.getProperties()).build();
    }
}
