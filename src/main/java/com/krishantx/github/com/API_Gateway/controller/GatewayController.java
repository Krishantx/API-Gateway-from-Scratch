package com.krishantx.github.com.API_Gateway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaServiceInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.krishantx.github.com.API_Gateway.service.DynamicRouting;
import com.krishantx.github.com.API_Gateway.service.JwtService;
import com.krishantx.github.com.API_Gateway.service.ServiceDiscovery;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class GatewayController {
  @Autowired
  private JwtService jwtService;
  @Autowired
  private DynamicRouting dynamicRouting;
  @Autowired
  private ServiceDiscovery serviceDiscovery;

  @RequestMapping("/**")
  public ResponseEntity<?> gatewayController(HttpServletRequest request) {
    String requestUri = request.getRequestURI();
    List<ServiceInstance> instances = serviceDiscovery.getServiceInstances(requestUri);
    ResponseEntity<?> response = dynamicRouting.requestInstances(instances, request);

    return response;
  }
}
