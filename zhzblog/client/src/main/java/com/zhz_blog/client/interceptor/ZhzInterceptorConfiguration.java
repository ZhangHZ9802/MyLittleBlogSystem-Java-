package com.zhz_blog.client.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ZhzInterceptorConfiguration implements WebMvcConfigurer {
    //注册一个bean，相当于我们之前写的一个bean标签
    //这个方法的名字，就相当于bean标签的id属性
    //这个方法的返回值，就相当于bean标签的class属性
    @Bean
    public ZhzInterceptor registerInterceptor() {
        return new ZhzInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(registerInterceptor()).addPathPatterns("/zhz/*");
    }
}
