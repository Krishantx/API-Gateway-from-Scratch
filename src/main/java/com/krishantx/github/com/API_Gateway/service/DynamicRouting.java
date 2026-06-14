package com.krishantx.github.com.API_Gateway.service;

import java.util.List;
import java.util.Random;

import org.springframework.cloud.client.ServiceInstance;
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

    Object body = this.restClient.get()
        .uri(requestURL)
        .retrieve()
        .body(Object.class);
    System.out.println(body);
    return ResponseEntity.status(200).body(body);

  }
}
