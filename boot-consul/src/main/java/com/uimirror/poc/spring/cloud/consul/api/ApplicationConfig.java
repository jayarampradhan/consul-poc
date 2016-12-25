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

import com.fasterxml.jackson.jaxrs.base.JsonParseExceptionMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.uimirror.poc.spring.cloud.consul.api.provider.ObjectMapperFactory;
import org.glassfish.jersey.message.DeflateEncoder;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.EncodingFilter;
import org.glassfish.jersey.server.filter.UriConnegFilter;
import org.glassfish.jersey.server.validation.ValidationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import static org.glassfish.jersey.server.ServerProperties.*;

/**
 * @author jpradhan
 *         2/16/16.
 */
@ApplicationPath("/api/v1")
@Configuration
public class ApplicationConfig extends ResourceConfig {


        public ApplicationConfig(){
//        packages(Boolean.TRUE, "com.uimirror.poc.spring.cloud.consul.api.resource");
//        appCtx.getBeansWithAnnotation(Path.class).forEach((name, resource) -> {
//                LOG.info(" -> {}", resource.getClass().getName());
//                register(resource);
//        });
		//App Specific Filter
//		register(ObjectMapperFactory.class);
        JacksonJaxbJsonProvider jsonProvider = new JacksonJaxbJsonProvider();
        jsonProvider.setMapper(ObjectMapperFactory.getObjectMapper());
        //Work arround to enable the jsonMappingExceptionmapper, as JacksonFeature was taking precedence
        //https://java.net/jira/browse/JERSEY-2722
        register(jsonProvider, MessageBodyReader.class, MessageBodyWriter.class);
//        register(JacksonFeature.class);



        //URL Path Negotation
        register(UriConnegFilter.class);
        //App Specific
        property(LANGUAGE_MAPPINGS, "english : en");
        property(APPLICATION_NAME, "consulPocAgent");
        //Register Application Event Listeners


        //Exception mappers
        register(JsonParseExceptionMapper.class);

        // Enable Tracing support.
        property(TRACING, "ALL");
        //To support GZIP encoding
        EncodingFilter.enableFor(this, GZipEncoder.class, DeflateEncoder.class);

	}
}
