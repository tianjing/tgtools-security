package com.github.tianjing.tgtools.security.sql.config;

import com.github.tianjing.tgtools.security.sql.bean.SecuritySqlInjectionConfigProperty;
import com.github.tianjing.tgtools.security.sql.filter.SqlInjectionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author 田径
 * @date 2019-12-21 20:33
 * @desc
 **/
public class SecuritySqlInjectionConfig {


    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public SqlInjectionFilter sqlInjectionFilter(SecuritySqlInjectionConfigProperty pSecuritySqlInjectionConfigProperty) {
        return new SqlInjectionFilter(pSecuritySqlInjectionConfigProperty);
    }


    @Bean
    public SecuritySqlInjectionConfigProperty securitySqlInjectionConfigProperty() {
        return new SecuritySqlInjectionConfigProperty();
    }

}
