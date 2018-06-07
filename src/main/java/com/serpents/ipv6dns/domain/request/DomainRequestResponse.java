package com.serpents.ipv6dns.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DomainRequestResponse {
    private UUID id;
}
