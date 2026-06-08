package com.krishantx.github.com.API_Gateway.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.krishantx.github.com.API_Gateway.entity.Route;

@Configuration
@ConfigurationProperties(prefix="")
public class RouteConfig {
        private Map<String, Route> routes = new HashMap<>();
        
        public Map<String, Route> getRoutes() { 
            return routes; 
        }
        
        public void setRoutes(Map<String, Route> routes) {
            this.routes = routes;
        }

        public Route findRoute(String incomingPath) {
            return routes.entrySet().stream()
            .filter(entry -> incomingPath.startsWith(entry.getKey()))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(null);
        }
}
