package com.sl.common.swagger.config;

import com.sl.common.swagger.annos.ApiJsonObject;
import com.sl.common.swagger.contexts.ApiJsonDocumentationContext;
import com.sl.common.swagger.contexts.ApiJsonDocumentationContextBuilder;
import com.sl.common.swagger.contexts.ApiJsonRefContext;
import com.sl.common.swagger.contexts.ApiJsonRefContextBuilder;
import com.sl.common.swagger.documentation.ApiJsonDocumentation;
import com.sl.common.swagger.scanners.ApiJsonDocumentationScanner;
import com.sl.common.swagger.scanners.ApiJsonRefModelReplacer;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

import static springfox.documentation.swagger.common.SwaggerPluginSupport.pluginDoesApply;

@Component
@Order
public class ApiJsonObjectModelsProvider implements OperationModelsProviderPlugin {

    //private static final Logger LOG = LoggerFactory.getLogger(ApiJsonObjectModelsProvider.class);

    private final TypeResolver typeResolver;
    private final ApiJsonDocumentationScanner scanner;
    private final ApiJsonRefModelReplacer replacer;


    @Autowired
    public ApiJsonObjectModelsProvider(ApiJsonDocumentationScanner scanner,
                                       TypeResolver typeResolver,
                                       ApiJsonRefModelReplacer replacer){
        this.scanner = scanner;
        this.typeResolver = typeResolver;
        this.replacer = replacer;
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return pluginDoesApply(delimiter);
    }

    @Override
    public void apply(RequestMappingContext context) {
        ApiJsonDocumentation documentation = collectApiJsonProperty(context);
        if (documentation != null){
            replaceApiJsonRef(apiJsonRefBuilder(context,documentation));
        }
    }

    private ApiJsonDocumentation collectApiJsonProperty(RequestMappingContext requestMappingContext){
        Optional<ApiJsonObject> annotation = requestMappingContext.findAnnotation(ApiJsonObject.class);
        if(annotation.isPresent()){
            ApiJsonObject apiJsonObject = annotation.get();
            ApiJsonDocumentationContext context = new ApiJsonDocumentationContextBuilder(requestMappingContext,apiJsonObject).build();
            return scanner.scan(context);
        }
        return null;
    }

    private void replaceApiJsonRef(ApiJsonRefContext apiJsonRefContext){
        replacer.replace(apiJsonRefContext);
    }

    private ApiJsonRefContext apiJsonRefBuilder(RequestMappingContext context, ApiJsonDocumentation documentation){
        return new ApiJsonRefContextBuilder(context,documentation,typeResolver).build();
    }

}
