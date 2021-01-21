package com.sl.common.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;

/**
 * swagger配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	/**
	 * 定义分隔符
	 */
	private static final String splitor = ";";
	@Autowired
	private ServletContext servletContext;
	@Value("${swagger.prefix}")
	private String prefix;
	@Value("${swagger.enable}")
	private Boolean enable;

	/**
	 * 创建API
	 */
	@Bean
	public Docket createRestApi()
	{
		return new Docket(DocumentationType.SWAGGER_2)
				.pathProvider(new RelativePathProvider(servletContext) {
					@Override
					public String getApplicationBasePath() {
						return prefix;
					}
				})
				.enable(enable)
				// 详细定制
				.apiInfo(apiInfo())
				.select()
				// 指定当前包路径
				.apis(Predicates.or(RequestHandlerSelectors.basePackage("com.sl.idripweb.controller"), RequestHandlerSelectors.basePackage("com.sl.common.controller")))
				 //扫描所有
//				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}

	/**
	 * 添加摘要信息
	 */
	private ApiInfo apiInfo()
	{
		// 用ApiInfoBuilder进行定制
		return new ApiInfoBuilder()
				.title("光谷电气idrip3.0接口文档")
				.description("描述：用于管理光谷电气基础版3.0接口文档")
				.contact(new Contact("关谷电气", null, null))
				.version("v0.0.1")
				.build();
	}
}