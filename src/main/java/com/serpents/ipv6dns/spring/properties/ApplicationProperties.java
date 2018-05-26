package com.serpents.ipv6dns.spring.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Properties;

@Component
public class ApplicationProperties {

    private final Properties properties = new Properties();
    private Resource applicationProperties;

    @Value(value = "classpath:application.properties")
    public void setApplicationProperties(final Resource applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @PostConstruct
    public void init() {
        try {
            properties.load(applicationProperties.getInputStream());
            properties.list(System.out);
        } catch (IOException ioe) {
            System.err.println("Application properties not loaded");
            throw new RuntimeException(ioe);
        }
    }

    public String getProperty(final ApplicationPropertiesKey property) {
        final String key = property.getKey();
        return this.properties.getProperty(key);
    }

    @PreDestroy
    public void destroy() {
        properties.clear();
    }
}
