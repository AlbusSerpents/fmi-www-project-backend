package com.serpents.ipv6dns.spring.config;

import com.serpents.ipv6dns.spring.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

import static com.serpents.ipv6dns.spring.properties.ApplicationPropertiesKey.SESSION_INACTIVITY_INTERVAL_IN_SECONDS;

@Configuration
@EnableSpringHttpSession
public class SessionsConfig {

    private final int timeoutInSeconds;

    @Autowired
    public SessionsConfig(final ApplicationProperties applicationProperties) {
        final String timeoutString = applicationProperties.getProperty(SESSION_INACTIVITY_INTERVAL_IN_SECONDS);
        timeoutInSeconds = Integer.parseInt(timeoutString);
    }


    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver();
    }

    @Bean
    public SessionRepository<ExpiringSession> sessionRepository() {
        MapSessionRepository repository = new MapSessionRepository();
        repository.setDefaultMaxInactiveInterval(timeoutInSeconds);
        return repository;
    }
}
