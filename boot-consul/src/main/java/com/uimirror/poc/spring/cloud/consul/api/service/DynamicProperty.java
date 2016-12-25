package com.uimirror.poc.spring.cloud.consul.api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;

/**
 * @author Jayaram
 *         9/28/16.
 */

public class DynamicProperty {
    @Autowired
    private Environment env;

    @Value("${key1}")
    private String test;

    public String getProperty(String key){
//        return env.getProperty(key);
        return test;
    }


}
