package com.krishantx.github.com.API_Gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.krishantx.github.com.API_Gateway.config.RouteConfig;
import com.krishantx.github.com.API_Gateway.entity.Route;
import com.krishantx.github.com.API_Gateway.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationFilter implements HandlerInterceptor {

  // Validate user using a Identity Provider Microservice
  @Autowired
  RouteConfig routeConfig;
  @Autowired
  JwtService jwtService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    // Convert JWT token to User Using an Identity Provider Service.
    Route route = routeConfig.findRoute(request.getRequestURI().substring(1));

    if (!route.isAuthRequired())
      return true;

    String token = request.getHeader("Authorization").substring(7);
    String username = jwtService.validateToken(token);
    System.out.println(username);
    if (username == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return false;
    }
    request.setAttribute("username", username);
    return true;
  }
}
