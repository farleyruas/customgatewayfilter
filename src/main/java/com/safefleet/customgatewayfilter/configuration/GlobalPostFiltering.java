package com.safefleet.customgatewayfilter.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class GlobalPostFiltering {

    @Bean
    public GlobalFilter postGlobalFiltering() {
        return ((exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            //log.info("Global post filter executed");
        })));
    }
}
