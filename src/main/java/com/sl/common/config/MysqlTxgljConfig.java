package com.sl.common.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Configuration
@MapperScan(basePackages = "com.sl.common.txgljdao", sqlSessionTemplateRef  = "txgljSqlSessionTemplate")
public class MysqlTxgljConfig {

    @Bean(name = "txgljDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.txglj")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "txgljSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("txgljDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "txgljTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("txgljDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "txgljSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("txgljSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    //配置Druid的监控
    //配置一个管理后台的Servlet
    @Bean(name = "txgljServletRegistrationBean")
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");

        Map<String, String> initParams = new HashMap<>();
        initParams.put("loginUsername","admin");    //登录名
        initParams.put("loginPassword","admin");    //登录密码
        initParams.put("allow","");//默认就是允许所有访问
        //initParams.put("deny","127.0.0.1");       //拒接访问
        registrationBean.setInitParameters(initParams);
        return registrationBean;
    }

    //配置一个web监控的filter
    @Bean(name = "txgljFilterRegistrationBean")
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());

        Map<String,String> initParams = new HashMap<>();
        initParams.put("exclusions","*.js,*.css,/druid/*");

        bean.setInitParameters(initParams);

        bean.setUrlPatterns(Arrays.asList("/*"));

        return  bean;
    }
}