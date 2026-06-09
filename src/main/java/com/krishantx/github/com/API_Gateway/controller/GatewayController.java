package com.krishantx.github.com.API_Gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.krishantx.github.com.API_Gateway.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class GatewayController {
    @Autowired
    private JwtService jwtService;
    @RequestMapping("/**")
    public ResponseEntity<?> gatewayController(HttpServletRequest request) {
        System.out.println(jwtService.generateToken("krishant"));
        return ResponseEntity.status(200).build();
    }
}
