package com.krishantx.github.com.API_Gateway.service;

import java.util.List;
import java.util.Random;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class DynamicRouting {
  private final RestClient restClient;

  public DynamicRouting(RestClient.Builder builder) {
    this.restClient = builder.build();
  }

  public ResponseEntity<?> requestInstances(List<ServiceInstance> instances, HttpServletRequest request) {
    ServiceInstance randomInstance = instances.get(new Random().nextInt(instances.size()));
    System.out.println(randomInstance.getUri());
    String requestURL = randomInstance.getUri() + "/" + request.getRequestURI();

    byte[] requestBody = null;
    Object responseBody = null;
    try {
      requestBody = request.getInputStream().readAllBytes();
    } catch (Exception e) {
      System.out.println(e);
    }
    try {
      System.out.println("contentType: " + request.getContentType());
      responseBody = this.restClient
          .method(HttpMethod.valueOf(request.getMethod()))
          .uri(requestURL)
          .contentType(MediaType.parseMediaType(request.getContentType()))
          .body(requestBody)
          .retrieve()
          .body(Object.class);
    } catch (Exception e) {
      System.out.println("Exception: " + e);
    }
    return ResponseEntity.status(200).body(responseBody);

  }
}
