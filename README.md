# spring-cloud-gateway-skywalking-duplicate-tradeid-demo

## About

This is a [Minimal, Reproducible Example](https://stackoverflow.com/help/minimal-reproducible-example) to recur a
skywalking agent bug: duplicate traceId when use forward scheme in spring.cloud.gateway.routes.uri properties, **base on skywalking-java-agent:9.1.0**

Reactor.netty.ioWorkerCount was set to 1 to recur this bug.

This bug happens in apm-spring-cloud-gateway-2.1.x-plugin, apm-spring-cloud-gateway-3.x-plugin and
apm-spring-cloud-gateway-4.x-plugin before version 9.1.0 (include 9.1.0).

apm-spring-cloud-gateway-2.0.x-plugin works as excepted.

## How To recur

1. start the gateway application
2. visit localhost:8080/generate_204 **3 times**
3. visit localhost:8080/forward  **3 times**
4. visit localhost:8080/generate_204 again  **3 times**

the logs print in console will be :

```log
2024-03-06 16:37:57.088  INFO 72346 --- [or-http-epoll-1] c.s.f.LoggingTraceIdGatewayFilterFactory : pathWithinApplication: /generate_204, current traceId: d7356bd80db94c12b5aae68d337e82da.60.17097142770450001
2024-03-06 16:37:58.395  INFO 72346 --- [or-http-epoll-1] c.s.f.LoggingTraceIdGatewayFilterFactory : pathWithinApplication: /generate_204, current traceId: d7356bd80db94c12b5aae68d337e82da.60.17097142783940005
2024-03-06 16:37:58.832  INFO 72346 --- [or-http-epoll-1] c.s.f.LoggingTraceIdGatewayFilterFactory : pathWithinApplication: /generate_204, current traceId: d7356bd80db94c12b5aae68d337e82da.60.17097142788300009
2024-03-06 16:38:01.045  INFO 72346 --- [or-http-epoll-1] c.s.f.LoggingTraceIdGatewayFilterFactory : pathWithinApplication: /forward-test, current traceId: d7356bd80db94c12b5aae68d337e82da.60.17097142810410013
2024-03-06 16:38:01.573  INFO 72346 --- [or-http-epoll-1] c.s.f.LoggingTraceIdGatewayFilterFactory : pathWithinApplication: /forward-test, current traceId: d7356bd80db94c12b5aae68d337e82da.60.17097142810410013
2024-03-06 16:38:02.014  INFO 72346 --- [or-http-epoll-1] c.s.f.LoggingTraceIdGatewayFilterFactory : pathWithinApplication: /forward-test, current traceId: d7356bd80db94c12b5aae68d337e82da.60.17097142810410013
2024-03-06 16:38:07.353  INFO 72346 --- [or-http-epoll-1] c.s.f.LoggingTraceIdGatewayFilterFactory : pathWithinApplication: /generate_204, current traceId: d7356bd80db94c12b5aae68d337e82da.60.17097142810410013
2024-03-06 16:38:07.817  INFO 72346 --- [or-http-epoll-1] c.s.f.LoggingTraceIdGatewayFilterFactory : pathWithinApplication: /generate_204, current traceId: d7356bd80db94c12b5aae68d337e82da.60.17097142810410013
2024-03-06 16:38:08.314  INFO 72346 --- [or-http-epoll-1] c.s.f.LoggingTraceIdGatewayFilterFactory : pathWithinApplication: /generate_204, current traceId: d7356bd80db94c12b5aae68d337e82da.60.17097142810410013
```

All traceId are duplicated after visit /forward , no matter visit /generate_204 or /forward.

## Why

A span was created at NettyRoutingFilterInterceptor#beforeMethod hook, and this span will stop at
HttpClientFinalizerSendInterceptor#beforeMethod hook.  
When use forward scheme in spring.cloud.gateway.routes.uri properties, a span was created before invoke
NettyRoutingFilter#filter method,
but NettyRoutingFilter don't handle this request because it has a forward scheme, and HttpClientFinalizer#send will
never be invoked, so this span will never be stopped and make this bug.

apm-spring-cloud-gateway-2.0.x-plugin works as excepted because this span was stopped in
NettyRoutingFilterInterceptor#afterMethod hook.