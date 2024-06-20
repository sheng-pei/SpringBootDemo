package org.example.springcloud.gateway.config;

import org.example.springcloud.gateway.filter.RequestHandleGlobalFilter;
import org.example.springcloud.gateway.handler.GlobalGatewayExceptionHandler;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public RequestHandleGlobalFilter requestHandleGlobalFilter() {
        return new RequestHandleGlobalFilter();
    }

    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/customer/**")
                        .uri("http://httpbin.org/get")
                ).build();
    }

    @Bean
    public GlobalGatewayExceptionHandler globalGatewayExceptionHandler() {
        return new GlobalGatewayExceptionHandler();
    }
}
