package com.serpents.ipv6dns.domain;

import com.serpents.ipv6dns.domain.Domain.DomainInfo;

import java.util.List;
import java.util.UUID;

public interface DomainRepository {

    DomainInfo findByCriteria(final String name, final String address);

    void insert(final Domain domain);

    List<Domain> findByOwner(final UUID clientId);

}
