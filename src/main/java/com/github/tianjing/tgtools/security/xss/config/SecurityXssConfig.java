package com.github.tianjing.tgtools.security.xss.config;

import com.github.tianjing.tgtools.security.xss.bean.SecurityXssConfigProperty;
import com.github.tianjing.tgtools.security.xss.filter.SecurityXssFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author 田径
 * @date 2019-12-21 20:33
 * @desc
 **/
public class SecurityXssConfig {


    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public SecurityXssFilter securityXssFilter(SecurityXssConfigProperty pSecurityXssConfigProperty) {
        return new SecurityXssFilter(pSecurityXssConfigProperty);
    }

    @Bean
    public SecurityXssConfigProperty securityXssConfigProperty() {
        return new SecurityXssConfigProperty();
    }

}
