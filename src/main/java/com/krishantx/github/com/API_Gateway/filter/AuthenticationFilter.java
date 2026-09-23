package com.krishantx.github.com.API_Gateway.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.krishantx.github.com.API_Gateway.config.RouteConfig;
import com.krishantx.github.com.API_Gateway.entity.Route;
import com.krishantx.github.com.API_Gateway.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Order(3)
@Component
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

  // Validate user using a Identity Provider Microservice
  @Autowired
  private RouteConfig routeConfig;
  @Autowired
  private JwtService jwtService;
  private static final Logger securityLogger = LoggerFactory.getLogger("SECURITY");

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    // Convert JWT token to User Using an Identity Provider Service.
    System.out.println("Request Reached the auth filter");
    Route route = routeConfig.findRoute(request.getRequestURI().substring(1));

    if (!route.isAuthRequired()) {
      securityLogger.info("No Authentication Required");
      filterChain.doFilter(request, response);
      return;
    }
    String token = request.getHeader("Authorization").substring(7);
    String username = jwtService.validateToken(token);
    if (username == null) {
      securityLogger.info("Unable to validate the user returning 401");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }
    request.setAttribute("username", username);
    log.info("\"{}\" is validated");
    filterChain.doFilter(request, response);
  }
}
