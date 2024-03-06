package org.cylx.springcloudgatewayskywalkingduplicatetradeiddemo.forward;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author syt
 * @since 2024/03/06 15:53
 */
@RequestMapping
@RestController
public class ForwardController {

    @GetMapping("/forward-test")
    public Mono<Void> forwardTest(ServerWebExchange serverWebExchange) {
        return serverWebExchange.getResponse().setComplete();
    }
}
