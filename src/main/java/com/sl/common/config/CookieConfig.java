package com.sl.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "cookie")
@Service
@Data
public class CookieConfig {
    private String name;
    private String path;
    private String domainName;
    private Boolean useHttpOnly;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Boolean getUseHttpOnly() {
        return useHttpOnly;
    }

    public void setUseHttpOnly(Boolean useHttpOnly) {
        this.useHttpOnly = useHttpOnly;
    }
}
