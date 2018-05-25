package com.serpents.ipv6dns.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcAutoConfiguration {

    private final PropertiesConfig propertiesConfig;

    @Autowired
    public WebMvcConfig(final PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        final BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        final String realmName = propertiesConfig.getProperty(PropertiesConfig.ApplicationProperty.REALM_NAME);
        entryPoint.setRealmName(realmName);
        return entryPoint;
    }

    @Bean
    public AbstractHttpSessionApplicationInitializer abstractHttpSessionApplicationInitializer() {
        return new AbstractHttpSessionApplicationInitializer() {
        };
    }
}
