package com.serpents.ipv6dns.domain;

import java.util.List;
import java.util.UUID;

public interface DomainRepository {

    DomainInfo findById(final UUID domainId);

    DomainInfo findByCriteria(final String name, final String address);

    DomainCreatedResponse insert(final Domain domain);

    List<Domain> findByOwner(final UUID clientId);

    Domain findByOwnerAndId(final UUID clientId, final UUID domainId);

    List<Domain> findAll(final DomainsSearch search);

}
