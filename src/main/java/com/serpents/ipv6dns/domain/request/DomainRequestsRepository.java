package com.serpents.ipv6dns.domain.request;

import com.serpents.ipv6dns.domain.request.DomainRequest.Identifier;

import java.util.List;
import java.util.UUID;

public interface DomainRequestsRepository {

    List<DomainRequest> findPending();

    DomainRequest findById(final UUID id);

    DomainRequestResponse insert(final DomainRequest request);

    boolean approve(final UUID id);

    boolean reject(final UUID id);

    boolean cancel(final Identifier identifier);
}
