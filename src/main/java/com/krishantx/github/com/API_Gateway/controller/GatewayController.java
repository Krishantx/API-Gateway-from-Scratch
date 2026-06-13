package com.krishantx.github.com.API_Gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.krishantx.github.com.API_Gateway.service.DynamicRouting;
import com.krishantx.github.com.API_Gateway.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class GatewayController {
  @Autowired
  private JwtService jwtService;
  @Autowired
  private DynamicRouting dynamicRouting;

  @RequestMapping("/**")
  public ResponseEntity<?> gatewayController(HttpServletRequest request) {
    // Use uri to discover where service is hosted using Service Discovery class
    // Use Dynamic Routing to dymamically pick a microservice and send an API
    // request to that service

    return ResponseEntity.status(200).build();
  }
}
