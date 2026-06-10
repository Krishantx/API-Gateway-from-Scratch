package com.krishantx.github.com.API_Gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.krishantx.github.com.API_Gateway.config.RouteConfig;
import com.krishantx.github.com.API_Gateway.entity.Route;
import com.krishantx.github.com.API_Gateway.utils.ValidationUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ValidationFilter implements HandlerInterceptor {
    @Autowired private RouteConfig routeConfig;

    private final ValidationUtil validationUtil = new ValidationUtil();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Checks if the HTTP Request is valid or not. Returns False if it is not
        /* Everything it needs to check:
            [x] Check if the endpoint exists
            [x] Check if the HTTP Method for that endpoint exists
            [x] Check if the request contains the required headers
        */
        
        String incomingRequest = request.getRequestURI().substring(1);
        Route route = routeConfig.findRoute(incomingRequest);
        // Check if the endpoint exists, If not return 404
        if (!validationUtil.endpointExists(route, request, response)) 
            return false;
        
        // Check if the endpoint supports that method
        if (!validationUtil.isMethodSupported(route, request, response)) 
            return false;
        
        
        //Check if the request contains the required headers
        if (!validationUtil.hasRequiredHeaders(route, request, response))
            return false;
        
        // If all checks pass -> move to the next filter chain
        return true;
        }
}
