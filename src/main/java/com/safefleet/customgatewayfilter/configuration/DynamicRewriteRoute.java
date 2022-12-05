package com.safefleet.customgatewayfilter.configuration;

import com.safefleet.customgatewayfilter.service.IDeviceUriService;
import com.safefleet.customgatewayfilter.service.IUnitUriService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;
import static org.springframework.http.HttpMethod.GET;

@Slf4j
@Configuration
public class DynamicRewriteRoute {

    private String backendUri;
    private final IDeviceUriService deviceUriService;
    private final IUnitUriService unitUriService;

    /**
     * Constructor
     *
     * @param backendUri
     * @param deviceUriService
     * @param unitUriService
     */
    public DynamicRewriteRoute(@Value("${gateway.apps.tenant-settings-api.uri}") String backendUri,
                               IDeviceUriService deviceUriService,
                               IUnitUriService unitUriService) {
        this.backendUri = backendUri;
        this.deviceUriService = deviceUriService;
        this.unitUriService = unitUriService;
    }


    @Bean
    public RouteLocator dynamicDeviceRoute(RouteLocatorBuilder builder) {
        this.backendUri = deviceUriService.getUrl();
        return builder.routes()
            .route("dynamicDeviceRewrite", r ->
                r.path("/")
                    .filters(f -> f.filter((exchange, chain) -> {
                        //this.backendUri = deviceUriService.getUrl();
                        return chain.filter(exchange);
                    }))
                    .uri(this.backendUri))
            .build();
    }


    public RouteLocator dynamicUnitRoute(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("dynamicUnitRewrite", r ->
                r.path("/v1/settings-api/units")
                    .filters(f -> f.filter((exchange, chain) -> {
                        ServerHttpRequest req = exchange.getRequest();
                        addOriginalRequestUrl(exchange, req.getURI());
                        String path = req.getURI().getRawPath();
                        String newPath = path.replace(
                                "/v1/settings-api/units",
                                unitUriService.getUrl());
                        log.info("path: "+path);
                        log.info("newPath: "+newPath);
                        ServerHttpRequest request = req.mutate()
                                .header("x-tenant-id", "public")
                                .path(newPath)
                                .build();
                        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
                        return chain.filter(exchange.mutate().request(request).build());
                    }))
                    .uri(backendUri))
            .build();
    }
}
