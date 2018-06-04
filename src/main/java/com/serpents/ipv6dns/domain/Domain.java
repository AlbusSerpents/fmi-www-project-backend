package com.serpents.ipv6dns.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Domain {
    private UUID id;
    private UUID owner;
    private DomainDetails domainDetails;
}
