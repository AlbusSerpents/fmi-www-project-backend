package com.serpents.ipv6dns.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DomainInfo {
    private final String address;
    private final String name;
}
