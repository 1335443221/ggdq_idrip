package com.sl.common.swagger.plugins;

import com.sl.common.swagger.contexts.ApiJsonDocumentationContext;
import org.springframework.plugin.core.Plugin;
import springfox.documentation.spi.DocumentationType;

public interface ApiJsonPropertyPlugin extends Plugin<DocumentationType> {

    void apply(ApiJsonDocumentationContext context);

}
