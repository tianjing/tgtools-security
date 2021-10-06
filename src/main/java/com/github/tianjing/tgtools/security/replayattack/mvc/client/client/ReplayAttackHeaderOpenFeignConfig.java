package com.github.tianjing.tgtools.security.replayattack.mvc.client.client;


import com.github.tianjing.tgtools.security.replayattack.mvc.client.interceptor.ReplayAttackHeaderInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * @author 田径
 * @date 2021-03-22 10:27
 * @desc
 **/
public class ReplayAttackHeaderOpenFeignConfig {

    @Bean
    public RequestInterceptor replayAttackHeaderInterceptor() {
        return new ReplayAttackHeaderInterceptor();
    }
}
