package com.microservice.product_service.common.constants;

public class AppConstant {
    public static final  String apiVersion="/api/v1";
    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/login",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/favicon.ico"
    };
}