package com.serpents.ipv6dns.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DomainCreatedResponse {
    private final UUID domainId;
    private final String domainName;
}
