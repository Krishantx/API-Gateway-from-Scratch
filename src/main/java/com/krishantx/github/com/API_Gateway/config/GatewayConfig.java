package com.krishantx.github.com.API_Gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.krishantx.github.com.API_Gateway.filter.AuthenticationFilter;
import com.krishantx.github.com.API_Gateway.filter.ValidationFilter;

@Configuration
public class GatewayConfig implements WebMvcConfigurer {
    private final ValidationFilter validationFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(validationFilter).order(1);
        registry.addInterceptor(new AuthenticationFilter()).order(2);
    }

    public GatewayConfig(ValidationFilter validationFilter) {
        this.validationFilter = validationFilter;
    }
}
