package com.uimirror.poc.spring.cloud.consul.api;


import com.uimirror.poc.spring.cloud.consul.api.conf.AppPropertyConfig;
import com.uimirror.poc.spring.cloud.consul.api.conf.AutoConfInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketMessagingAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.Instant;
import java.util.Arrays;

/**
 * @author Jayaram
 *         8/6/16.
 */
@Configuration
@ComponentScan(basePackages = {"com.uimirror.poc.spring.cloud.consul"})
@Import({
        AppPropertyConfig.class,
        AutoConfInitializer.class
})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class
        , JpaRepositoriesAutoConfiguration.class,  SessionAutoConfiguration.class
        , MessageSourceAutoConfiguration.class, WebSocketAutoConfiguration.class
        , WebMvcAutoConfiguration.class , WebSocketMessagingAutoConfiguration.class
        , HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class
        , JtaAutoConfiguration.class, JacksonAutoConfiguration.class})
public class StartApp extends SpringBootServletInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(StartApp.class);


    public static void main (final String[] args) {

        LOG.info("CmdLine Args {}", Arrays.asList(args));
        try {
            new SpringApplicationBuilder()
                    .sources(StartApp.class)
                    .web(Boolean.TRUE)
                    .run(args);
            LOG.info("Consul Agent API Started on: {}, Its Ready to use, have fun.", Instant.now());
        } catch (Exception e) {
            System.out.println("What's wrong"+e.getMessage());
            LOG.error("Unexpected error", e);
        }
    }
}
