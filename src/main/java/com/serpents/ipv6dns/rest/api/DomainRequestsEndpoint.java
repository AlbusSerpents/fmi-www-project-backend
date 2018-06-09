package com.serpents.ipv6dns.rest.api;

import com.serpents.ipv6dns.domain.DomainCreatedResponse;
import com.serpents.ipv6dns.domain.DomainDetails;
import com.serpents.ipv6dns.domain.request.DomainRequest;
import com.serpents.ipv6dns.domain.request.DomainRequest.Identifier;
import com.serpents.ipv6dns.domain.request.DomainRequestApproval;
import com.serpents.ipv6dns.domain.request.DomainRequestResponse;
import com.serpents.ipv6dns.domain.request.DomainRequestsService;
import com.serpents.ipv6dns.spring.user.details.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/request")
public class DomainRequestsEndpoint {

    private final DomainRequestsService service;

    @Autowired
    public DomainRequestsEndpoint(final DomainRequestsService service) {
        this.service = service;
    }

    @ResponseStatus(CREATED)
    @RequestMapping(value = "", method = POST, consumes = "application/json", produces = "application/json")
    public DomainRequestResponse requestDomain(
            final @AuthenticationPrincipal UserDetailsImpl details,
            final @RequestBody @Valid DomainDetails domainDetails) {
        final UUID userId = details.getUserId();
        return service.requestDomain(userId, domainDetails);
    }

    @ResponseStatus(OK)
    @RequestMapping(value = "", method = GET, produces = "application/json")
    public List<DomainRequest> getPending() {
        return service.listPending();
    }

    @ResponseStatus(OK)
    @RequestMapping(value = "/{requestId}", method = GET, produces = "application/json")
    public DomainRequest getRequest(
            final @AuthenticationPrincipal UserDetailsImpl details,
            final @PathVariable(name = "requestId") UUID requestId) {
        final Identifier identifier = new Identifier(requestId, details.getUserId());
        return service.readByIdentifier(identifier);
    }

    @ResponseStatus(NO_CONTENT)
    @RequestMapping(value = "/{requestId}", method = DELETE)
    public void cancel(
            final @AuthenticationPrincipal UserDetailsImpl details,
            final @PathVariable(name = "requestId") UUID requestId) {
        final Identifier identifier = new Identifier(requestId, details.getUserId());
        service.cancel(identifier);
    }

    @ResponseStatus(CREATED)
    @RequestMapping(value = "/{requestId}/approve", method = POST, consumes = "application/json")
    public DomainCreatedResponse approve(
            final @PathVariable(name = "requestId") UUID requestId,
            final @RequestBody DomainRequestApproval approval) {
        return service.approve(requestId, approval);
    }

    @ResponseStatus(NO_CONTENT)
    @RequestMapping(value = "/{requestId}/reject", method = POST)
    public void reject(final @PathVariable(name = "requestId") UUID requestId) {
        service.rejectRequest(requestId);
    }
}
