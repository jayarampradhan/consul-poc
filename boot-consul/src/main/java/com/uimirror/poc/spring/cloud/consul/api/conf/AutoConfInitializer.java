package com.uimirror.poc.spring.cloud.consul.api.conf;


import com.uimirror.poc.spring.cloud.consul.api.service.DynamicProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jersey.ResourceConfigCustomizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.consul.config.ConfigWatch;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 * @author Jayaram
 *         2/18/16.
 */
@Configuration
@EnableDiscoveryClient
@EnableScheduling
public class AutoConfInitializer {

    @Inject
    private ApplicationContext appCtx;
    private static final Logger LOG = LoggerFactory.getLogger(AutoConfInitializer.class);
    @Bean
    public ResourceConfigCustomizer jersey() {
        return config -> {
            LOG.info("Jersey resource classes found:");
            appCtx.getBeansWithAnnotation(Path.class).forEach((name, resource) -> {
                LOG.info(" -> {}", resource.getClass().getName());
                config.register(resource);
            });
        };
    }

    @Bean
    @RefreshScope
    public static DynamicProperty dynamicProperty(){
        return new DynamicProperty();
    }

}
