package com.sl.common.swagger.documentation;

import com.sl.common.swagger.schema.ApiJsonPropertySingle;

import java.util.List;

public class ApiJsonDocumentation {

    private final String name;
    private final List<ApiJsonPropertySingle> properties;

    public ApiJsonDocumentation(String name, List<ApiJsonPropertySingle> properties){
        this.name = name;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public List<ApiJsonPropertySingle> getProperties() {
        return properties;
    }
}
