package com.sl.common.swagger.plugins;

import com.sl.common.swagger.contexts.ApiJsonRefContext;
import com.sl.common.swagger.schema.ApiJsonPropertySingle;
import com.sl.common.swagger.utils.SwaggerASMUtil;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;

import java.util.List;

import static springfox.documentation.swagger.common.SwaggerPluginSupport.pluginDoesApply;

@Component
public class ApiJsonRefModelHeaderReplacer implements ApiJsonRefModelReplacePlugin {
    @Override
    public Integer order() {
        return 0;
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return pluginDoesApply(delimiter);
    }

    @Override
    public void apply(ApiJsonRefContext context) {
        String name = context.getDocumentation().getName();
        List<ApiJsonPropertySingle> properties = context.getDocumentation().getProperties();
        byte[] code = SwaggerASMUtil.createRefModel(properties, name);
        if(code != null) context.setCode(code);
    }
}
