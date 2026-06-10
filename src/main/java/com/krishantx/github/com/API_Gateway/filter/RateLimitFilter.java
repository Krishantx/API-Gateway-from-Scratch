package com.krishantx.github.com.API_Gateway.filter;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.HandlerInterceptor;

import com.krishantx.github.com.API_Gateway.entity.RateLimiterBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitFilter implements HandlerInterceptor{

    private final RestClient rlaasClient; 

    public RateLimitFilter(RestClient rlaasClient) {
        this.rlaasClient = rlaasClient;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // return true;
        RateLimiterBody body = 
                new RateLimiterBody(request.getMethod(), 
                                    request.getRequestURI(), 
                                    request.getRemoteAddr());

        ResponseEntity<Void> rlaasResponse;
        try {
        rlaasResponse = rlaasClient.post()
                .uri("/check")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();   
        } catch (HttpClientErrorException.TooManyRequests e) {
            response.setStatus(429);
            return false;
        }
        if (rlaasResponse.getStatusCode() == HttpStatusCode.valueOf(200))
            return true;

        System.out.println(rlaasResponse.getStatusCode());
        return true;
    }
}
