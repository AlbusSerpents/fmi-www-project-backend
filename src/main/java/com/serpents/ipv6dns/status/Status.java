package com.serpents.ipv6dns.status;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Data
@AllArgsConstructor
public class Status {
    private final String applicationName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss ", shape = STRING)
    private final LocalDateTime now;
    private final ApplicationStatus status;

    public enum ApplicationStatus {
        RUNNING, DOWN
    }
}
