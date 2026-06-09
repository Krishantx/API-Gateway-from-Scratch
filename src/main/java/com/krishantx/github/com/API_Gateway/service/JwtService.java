package com.krishantx.github.com.API_Gateway.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.krishantx.github.com.API_Gateway.utils.JwtUtil;

@Service
public class JwtService {
    private JwtUtil jwtUtil = new JwtUtil();

    public String generateToken(String username) {
        return jwtUtil.generateToken(username);
    }

    public String validateToken(String token) {
        String username = jwtUtil.getUsername(token);
        Date exp = jwtUtil.getExpiration(token);
        Date currDate = new Date(System.currentTimeMillis());
        System.out.println(exp);
        System.out.println(currDate);
        if (currDate.after(exp)) {
            return null;
        }
        return username;

    }
}
