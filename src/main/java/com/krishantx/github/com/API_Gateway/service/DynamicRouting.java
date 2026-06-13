package com.krishantx.github.com.API_Gateway.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.krishantx.github.com.API_Gateway.config.RouteConfig;
import com.krishantx.github.com.API_Gateway.entity.Route;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;

@Service
public class DynamicRouting {
  @Autowired
  private DiscoveryClient discoveryClient;
  @Autowired
  private RouteConfig routeConfig;

  private String getServiceName(String uri) {
    Route route = routeConfig.findRoute(uri);
    return route.getServiceName();
  }

  public InstanceInfo getInstance(String uri) {
    String serviceName = getServiceName(uri);
    List<InstanceInfo> instances = discoveryClient.getInstancesById(serviceName);
    int randomInt = Random.nextInt(instances.size());
    InstanceInfo instance = instances.get(randomInt);
    return instance;
  }

}
