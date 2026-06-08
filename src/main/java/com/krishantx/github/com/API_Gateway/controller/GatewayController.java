package com.krishantx.github.com.API_Gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class GatewayController {

    @RequestMapping("/**")
    public ResponseEntity<?> gatewayController(HttpServletRequest request) {
        return ResponseEntity.status(200).build();
    }
}
