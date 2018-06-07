package com.serpents.ipv6dns.domain.request;

import java.util.UUID;

public interface DomainRequestsRepository {

    DomainRequest findById(final UUID requestId);

    DomainRequestResponse insert(final DomainRequest request);

    boolean updateStatus(final UUID requestId, final DomainRequestStatus newStatus);
}
