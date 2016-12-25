package com.uimirror.poc.spring.cloud.consul.api.provider;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * This will be customizing the JAXB behaviour such as
 * <ul>
 *     <li>Removing <code>null</code> value from the json and xml response</li>
 *     <li>Use Jaxb annotations</li>
 *     <li>Use line indent for formatting json look</li>
 * </ul>
 * @author Jayaram
 */
public class ObjectMapperFactory {

	private static final ObjectMapper customObjectMapper = createCustomMapper();

    private ObjectMapperFactory(){
        throw new UnsupportedOperationException();
    }

	public static ObjectMapper createCustomMapper() {
		return new ObjectMapper()
			.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
			.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
			.enable(SerializationFeature.INDENT_OUTPUT)
			.setSerializationInclusion(JsonInclude.Include.NON_NULL)
			.setAnnotationIntrospector(createJaxbJacksonAnnotationIntrospector())
			.registerModule(customAdptersAsModules())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            // add JAXB annotation support if required
			.registerModule(new JaxbAnnotationModule());
	}

	private static SimpleModule customAdptersAsModules(){
		JavaTimeModule javaTimeModule=new JavaTimeModule();
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC)));
		javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_DATE.withZone(ZoneOffset.UTC)));
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC)));
		javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_DATE.withZone(ZoneOffset.UTC)));
		return javaTimeModule;
	}

	private static AnnotationIntrospector createJaxbJacksonAnnotationIntrospector() {

		final AnnotationIntrospector jaxbIntrospector = new JaxbAnnotationIntrospector(TypeFactory.defaultInstance());
		final AnnotationIntrospector jacksonIntrospector = new JacksonAnnotationIntrospector();

		return AnnotationIntrospector.pair(jacksonIntrospector, jaxbIntrospector);
	}

	public static ObjectMapper getObjectMapper() {
		return customObjectMapper;
	}
}

