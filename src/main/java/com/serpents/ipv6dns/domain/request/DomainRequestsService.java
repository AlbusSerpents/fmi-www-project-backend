package com.serpents.ipv6dns.domain.request;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DomainRequestsService {

    public DomainRequestResponse requestDomain(final UUID clientId, final DomainRequest request) {

        return null;
    }

    public void cancelRequest(final UUID requestId, final UUID clientId) {

    }

    public void approveRequest(final UUID requestId, final DomainRequestApproval approval) {

    }

    public void rejectRequest(final UUID requestId) {

    }
}
