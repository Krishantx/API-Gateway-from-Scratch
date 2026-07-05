package com.krishantx.github.com.API_Gateway.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.filter.OncePerRequestFilter;

import com.krishantx.github.com.API_Gateway.entity.RateLimiterBody;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RateLimitFilter extends OncePerRequestFilter {

  private final RestClient rlaasClient;

  public RateLimitFilter(RestClient rlaasClient) {
    this.rlaasClient = rlaasClient;
  }

  @Override
  @Order(4)
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    // return true;
    RateLimiterBody body = new RateLimiterBody(request.getMethod(),
        request.getRequestURI(),
        request.getRemoteAddr());

    ResponseEntity<Void> rlaasResponse;
    log.info("Checking Rate Limit for the URI: \"{}\" and Remote Addr: \"{} \"",
        request.getRequestURI(),
        request.getRemoteAddr());
    try {
      rlaasResponse = rlaasClient.post()
          .uri("/check")
          .contentType(MediaType.APPLICATION_JSON)
          .body(body)
          .retrieve()
          .toBodilessEntity();
    } catch (HttpClientErrorException.TooManyRequests e) {
      log.info("User is rate limited for the endpoint {}", request.getRequestURI());
      response.setStatus(429);
      return;
    }

    if (rlaasResponse.getStatusCode() == HttpStatusCode.valueOf(200)) {
      log.info("User is not rate limited proceeding to the controller");
      filterChain.doFilter(request, response);
    }

  }
}
