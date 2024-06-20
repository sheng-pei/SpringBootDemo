package org.example.springcloud.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class RequestHandleGlobalFilter implements GlobalFilter, Ordered {

    public RequestHandleGlobalFilter() {}

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("nio" + Thread.currentThread().getId());
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//
//        }
        throw new IllegalArgumentException("nio error");
//        String token = exchange.getRequest().getQueryParams().getFirst("token");
//        if (token == null || token.isEmpty()) {
////            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//                System.out.println(Thread.currentThread().getId());
////                try {
////                    Thread.sleep(20000);
////                } catch (InterruptedException e) {
////                }
//                throw new IllegalArgumentException("hello world");
////                return "hello world";
//            });
//            return exchange.getResponse().writeWith(Mono.fromFuture(future).map(s -> exchange.getResponse().bufferFactory().wrap(s.getBytes())));
//        }
//        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
