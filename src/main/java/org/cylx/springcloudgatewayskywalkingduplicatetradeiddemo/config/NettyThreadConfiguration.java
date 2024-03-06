package org.cylx.springcloudgatewayskywalkingduplicatetradeiddemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorResourceFactory;

@Configuration
public class NettyThreadConfiguration {

    @Bean
    public ReactorResourceFactory reactorClientResourceFactory() {
        // reactor netty workerCount was set to 1
        // to recur the bug better
        System.setProperty("reactor.netty.ioWorkerCount","1");
        return new ReactorResourceFactory();
    }
}
