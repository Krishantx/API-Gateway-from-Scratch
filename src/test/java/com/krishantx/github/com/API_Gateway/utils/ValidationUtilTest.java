package com.krishantx.github.com.API_Gateway.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class ValidationUtilTest {

  private ValidationUtil validationUtil;
  private HttpServletRequest request;
  private HttpServletResponse response;

  @BeforeEach
  void constr() {
    validationUtil = new ValidationUtil();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
  }

  @Test
  void endpointExists_nullRoute_returns404AndFalse() {
    boolean result = validationUtil.endpointExists(null, request, response);
    assertEquals(false, result);
    assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
  }
}
