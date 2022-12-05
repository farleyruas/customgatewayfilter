package com.safefleet.customgatewayfilter.configuration;

import com.safefleet.customgatewayfilter.service.IUnitUriService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;


@Slf4j
@Component
public class DynamicForwardGatewayFilterFactory extends AbstractGatewayFilterFactory<DynamicForwardGatewayFilterFactory.ConfigArgs> {

    private final IUnitUriService unitUriService;

    public DynamicForwardGatewayFilterFactory(IUnitUriService unitUriService) {
        super(DynamicForwardGatewayFilterFactory.ConfigArgs.class);
        this.unitUriService = unitUriService;
    }

    @Override
    public GatewayFilter apply(ConfigArgs config) {

        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            URI redirectURI = null;
            redirectURI = URI.create("http://youtube.com");

            ServerHttpRequest modifiedRequest = exchange
                    .getRequest()
                    .mutate()
                    .uri(redirectURI)
                    .build();

            ServerWebExchange modifiedExchange = exchange
                    .mutate()
                    .request(modifiedRequest)
                    .build();

            modifiedExchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, redirectURI);
            return chain.filter(modifiedExchange);
        };
    }

    public static class ConfigArgs {

        private String status;
        private String url;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
