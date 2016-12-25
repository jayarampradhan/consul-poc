package com.uimirror.poc.spring.cloud.consul.api.resource;


import com.uimirror.poc.spring.cloud.consul.api.service.DynamicProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Jayaram
 *         8/6/16.
 */
@Component
@Singleton
@Path("/key")
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
public class KeyResource {

    private static final Logger LOG = LoggerFactory.getLogger(KeyResource.class);

    @Value("${git.sha:NA}")
    private String currentBuildNo;
    @Value("${build.date:NA}")
    private String buildDateTime;
    @Value("${build.version:NA}")
    private String projectVersion;

    @Autowired
    private DynamicProperty dynamicProperty;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public TestResponse getKey() {
        TestResponse rs = new TestResponse();
        rs.key = "Test";
        rs.value = "Test";
        LOG.info("Object is Refreshed"+dynamicProperty);
        LOG.info("Key Value is --{}",dynamicProperty.getProperty("key1"));
        return rs;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TestResponse {
        @XmlElement(name = "key")
        private String key;
        @XmlElement(name = "value")
        private String value;
    }
}
