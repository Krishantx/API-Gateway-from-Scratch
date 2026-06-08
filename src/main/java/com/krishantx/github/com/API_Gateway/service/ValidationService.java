package com.krishantx.github.com.API_Gateway.service;

import java.util.Enumeration;
import java.util.List;

import org.springframework.stereotype.Service;

import com.krishantx.github.com.API_Gateway.entity.Route;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ValidationService {
    
    public boolean endpointExists(Route route, HttpServletRequest request, HttpServletResponse response) {
       if (route == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }
        return true;
    }

    public boolean isMethodSupported(Route route, HttpServletRequest request, HttpServletResponse response) {
        String incomingMethod = request.getMethod();
        if (!route.getAllowedMethods().contains(incomingMethod)) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return false;
        }
        return true;
    }

    public boolean hasRequiredHeaders(Route route, HttpServletRequest request, HttpServletResponse response) {
        List<String> requiredHeaders = route.getRequiredHeaders();
        if (requiredHeaders == null)
            return true;
        
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
        return true;
    }


}
