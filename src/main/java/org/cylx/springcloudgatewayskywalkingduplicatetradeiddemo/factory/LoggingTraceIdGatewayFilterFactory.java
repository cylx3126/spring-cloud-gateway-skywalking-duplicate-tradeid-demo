package org.cylx.springcloudgatewayskywalkingduplicatetradeiddemo.factory;

import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.webflux.WebFluxSkyWalkingTraceContext;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingTraceIdGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            log.info("pathWithinApplication: {}, current traceId: {}",
                    exchange.getRequest().getPath().pathWithinApplication(),
                    WebFluxSkyWalkingTraceContext.traceId(exchange));
            return chain.filter(exchange);
        };
    }
}
