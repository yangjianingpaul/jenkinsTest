package com.heima.app.gateway.filter;


import com.heima.app.gateway.util.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizeFilter implements Ordered, GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.Get the request and response objects
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //2.Determine whether to log in
        if(request.getURI().getPath().contains("/login")){
            //pass
            return chain.filter(exchange);
        }

        //3.Get token
        String token = request.getHeaders().getFirst("token");

        //4.Check whether the token exists
        if(StringUtils.isBlank(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //5.Check whether the token is valid
        try {
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            //Whether it is expired or not
            int result = AppJwtUtil.verifyToken(claimsBody);
            if(result == 1 || result  == 2){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

//            Get user information
            Object userId = claimsBody.get("id");
//            Store in header
            ServerHttpRequest serverHttpRequest = request.mutate().headers(httpHeaders -> {
                httpHeaders.add("userId", userId + "");
            }).build();

            exchange.mutate().request(serverHttpRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //6.pass
        return chain.filter(exchange);
    }

    /**
     * Priority setting:  A smaller value indicates a higher priority
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
