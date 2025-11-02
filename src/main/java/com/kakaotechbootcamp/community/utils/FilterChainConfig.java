package com.kakaotechbootcamp.community.utils;

import com.kakaotechbootcamp.community.utils.security.filter.CorsFilter;
import com.kakaotechbootcamp.community.utils.security.filter.ErrorHandlingFilter;
import com.kakaotechbootcamp.community.utils.security.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterChainConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> registrationCorsFilter(CorsFilter corsFilter) {
        FilterRegistrationBean<CorsFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(corsFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<ErrorHandlingFilter> registrationExceptionHandlingFilter(
        ErrorHandlingFilter errorHandlingFilter) {
        FilterRegistrationBean<ErrorHandlingFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(errorHandlingFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(2);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> registrationSessionAuthFilter(
        JwtAuthFilter jwtAuthFilter) {
        FilterRegistrationBean<JwtAuthFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(jwtAuthFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(3);
        return filterRegistrationBean;
    }
}
