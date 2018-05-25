package com.serpents.ipv6dns.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Properties;

@SuppressWarnings("unused")
@Component
public class PropertiesConfig {

    public enum ApplicationProperty {
        REALM_NAME("security.realm.name"),
        SESSION_INACTIVITY_INTERVAL_IN_SECONDS("session.timeout.seconds"),
        JDBC_URL("database.url");

        ApplicationProperty(final String key) {
            this.key = key;
        }

        private final String key;
    }

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

    public String getProperty(final ApplicationProperty property) {
        final String key = property.key;
        return this.properties.getProperty(key);
    }

    @PreDestroy
    public void destroy() {
        properties.clear();
    }
}
