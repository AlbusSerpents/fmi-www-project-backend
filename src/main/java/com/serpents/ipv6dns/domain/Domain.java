package com.serpents.ipv6dns.domain;

import com.serpents.ipv6dns.address.Address;
import com.serpents.ipv6dns.domain.request.DomainRequestInfo;
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

    public Domain(final DomainRequestInfo info, final Address address) {
        this(null, info.getClientId(), info.getDetails(), address);
    }
}
