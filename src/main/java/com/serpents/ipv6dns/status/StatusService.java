package com.serpents.ipv6dns.status;

import com.serpents.ipv6dns.spring.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.atomic.AtomicReference;

import static com.serpents.ipv6dns.spring.properties.ApplicationPropertiesKey.APPLICATION_NAME;
import static com.serpents.ipv6dns.status.ApplicationStatus.DOWN;
import static com.serpents.ipv6dns.status.ApplicationStatus.RUNNING;
import static java.time.LocalDateTime.now;

@Service
public class StatusService {
    private final AtomicReference<ApplicationStatus> applicationStatus;

    private final String applicationName;

    @Autowired
    public StatusService(final ApplicationProperties properties) {
        applicationStatus = new AtomicReference<>(DOWN);
        this.applicationName = properties.getProperty(APPLICATION_NAME);
    }

    @PostConstruct
    public void init() {
        applicationStatus.set(RUNNING);
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus.get();
    }

    public Status getStatus() {
        return new Status(applicationName, now(), applicationStatus.get());
    }

    public void start() {
        applicationStatus.set(RUNNING);
    }

    public void stop() {
        applicationStatus.set(DOWN);
    }

    @PreDestroy
    public void destroy() {
        applicationStatus.set(DOWN);
    }
}
