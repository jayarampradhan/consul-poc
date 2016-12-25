package com.uimirror.poc.spring.cloud.consul.api;
/*
 * Copyright (c) 1983 - 2016, Intuit and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Intuit or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
 * @author jpradhan
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
