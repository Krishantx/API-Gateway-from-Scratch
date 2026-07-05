package com.krishantx.github.com.API_Gateway.service;

import java.util.Date;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private static final String SECRET = "SUPER_SECRET_KEY_THAT_IS_VARY_LONG_AND_SECURE";
  private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
  private static final long EXPIRATION_TIME = 30 * 60 * 1000;

  public String validateToken(String token) {
    String username = getUsername(token);
    Date exp = getExpiration(token);
    Date currDate = new Date(System.currentTimeMillis());
    if (currDate.after(exp)) {
      return null;
    }
    return username;

  }

  public static String generateToken(String username) {
    return Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(KEY)
        .compact();
  }

  public String getUsername(String token) {
    return getClaims(token).getSubject();
  }

  public Date getExpiration(String token) {
    return getClaims(token).getExpiration();
  }

  public Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(KEY)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
