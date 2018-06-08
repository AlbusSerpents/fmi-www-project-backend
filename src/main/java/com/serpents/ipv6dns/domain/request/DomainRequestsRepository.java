package com.serpents.ipv6dns.domain.request;

import com.serpents.ipv6dns.domain.request.DomainRequest.Identifier;

import java.util.UUID;

public interface DomainRequestsRepository {

    DomainRequest findByIdentifier(final Identifier identifier);

    DomainRequest findById(final UUID id);

    DomainRequestResponse insert(final DomainRequest request);

    boolean updateStatus(final UUID requestId, final DomainRequestStatus newStatus);
}
