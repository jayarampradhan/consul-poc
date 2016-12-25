package com.uimirror.poc.spring.cloud.consul.api;


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
 * @author Jayaram
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
