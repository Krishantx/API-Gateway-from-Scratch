package com.krishantx.github.com.API_Gateway.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import com.krishantx.github.com.API_Gateway.config.RouteConfig;
import com.krishantx.github.com.API_Gateway.entity.Route;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.converters.Auto;

public class ServiceDiscovery {
  @Autowired
  private DiscoveryClient discoveryClient;
  @Autowired
  private RouteConfig routeConfig;

  private String getServiceName(String uri) {
    Route route = routeConfig.findRoute(uri);
    return route.getServiceName();
  }

  public List<InstanceInfo> getServiceInstances(String uri) {
    String serviceName = getServiceName(uri);
    List<InstanceInfo> instances = discoveryClient.getInstancesById(serviceName);
    return instances;
  }

}
