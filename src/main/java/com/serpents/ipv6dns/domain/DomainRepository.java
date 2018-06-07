package com.serpents.ipv6dns.domain;

import java.util.UUID;

public interface DomainRepository {

    Domain findById(final UUID domainId);

    DomainCreatedResponse insert(final Domain domain);
}
