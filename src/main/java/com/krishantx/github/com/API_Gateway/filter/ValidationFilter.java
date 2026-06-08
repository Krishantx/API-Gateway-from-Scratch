package com.krishantx.github.com.API_Gateway.filter;

import java.util.Enumeration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.krishantx.github.com.API_Gateway.config.RouteConfig;
import com.krishantx.github.com.API_Gateway.entity.Route;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ValidationFilter implements HandlerInterceptor {
    @Autowired
    RouteConfig routeConfig;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Checks if the HTTP Request is valid or not. Returns False if it is not
        /* Everything it needs to check:
            [x] Check if the endpoint exists
            [x] Check if the HTTP Method for that endpoint exists
            [x] Check if the request contains the required headers
            [] Check if the request contains the necessary body components
        */

        // Check if the endpoint exists, If not return 404
       String incomingRequest = request.getRequestURI().substring(1);
       Route routeInfo = routeConfig.findRoute(incomingRequest);
       if (routeInfo == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }

        // Check if the endpoint supports that method
        String incomingMethod = request.getMethod();
        if (!routeInfo.getAllowedMethods().contains(incomingMethod)) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return false;
        }
        
        //Check if the request contains the required headers
        List<String> requiredHeaders = routeInfo.getRequiredHeaders();
        int matchedHeadersCount = 0;
        int requiredHeadersCount = requiredHeaders.size();
        Enumeration<String> incomingHeaders = request.getHeaderNames();
        while (incomingHeaders.hasMoreElements()) {
            if (requiredHeaders.contains(incomingHeaders.nextElement())) {
                matchedHeadersCount++;
            }
            if (matchedHeadersCount == requiredHeadersCount) {
                break;
            }
        }
        if (matchedHeadersCount != requiredHeadersCount) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }

        //Check if the request contains the necessary body components

        return true;
        
        }
}
