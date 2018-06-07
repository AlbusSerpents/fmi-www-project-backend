package com.serpents.ipv6dns.domain;

import com.serpents.ipv6dns.address.Address;
import com.serpents.ipv6dns.domain.request.DomainRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Domain {
    private final UUID id;
    private final UUID owner;
    private final DomainDetails domainDetails;
    private final Address address;

    public Domain(final DomainRequest request, final Address address) {
        this(null, request.getClientId(), request.getDomainDetails(), address);
    }
}
