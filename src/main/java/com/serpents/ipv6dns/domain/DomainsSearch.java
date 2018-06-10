package com.serpents.ipv6dns.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class DomainsSearch {
    private UUID clientId;
    private String name;
    private String address;
}
