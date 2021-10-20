package com.github.tianjing.tgtools.security.csrf.config;

import com.github.tianjing.tgtools.security.csrf.bean.SecurityCsrfConfigProperty;
import com.github.tianjing.tgtools.security.csrf.filter.CsrfHostFilter;
import com.github.tianjing.tgtools.security.csrf.filter.CsrfOriginFilter;
import com.github.tianjing.tgtools.security.csrf.filter.CsrfRefererFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author 田径
 * @date 2019-12-21 20:33
 * @desc
 **/
public class SecurityCsrfConfig {


    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public CsrfRefererFilter csrfRefererFilter(SecurityCsrfConfigProperty pSecurityCsrfConfigProperty) {
        return new CsrfRefererFilter(pSecurityCsrfConfigProperty);
    }

    @Bean
    public CsrfOriginFilter csrfOriginFilter(SecurityCsrfConfigProperty pSecurityCsrfConfigProperty) {
        return new CsrfOriginFilter(pSecurityCsrfConfigProperty);
    }

    @Bean
    public CsrfHostFilter csrfHostFilter(SecurityCsrfConfigProperty pSecurityCsrfConfigProperty) {
        return new CsrfHostFilter(pSecurityCsrfConfigProperty);
    }

    @Bean
    public SecurityCsrfConfigProperty securityCsrfConfigProperty() {
        return new SecurityCsrfConfigProperty();
    }

}
