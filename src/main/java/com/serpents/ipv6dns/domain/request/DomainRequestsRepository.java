package com.serpents.ipv6dns.domain.request;

import java.util.List;
import java.util.UUID;

public interface DomainRequestsRepository {

    List<DomainRequestInfo> findPending();

    DomainRequestInfo findById(final UUID id);

    DomainRequestResponse insert(final DomainRequest request);

    boolean approve(final UUID id);

    boolean reject(final UUID id);
}
