package com.serpents.ipv6dns.rest.api;

import com.serpents.ipv6dns.domain.request.DomainRequest;
import com.serpents.ipv6dns.domain.request.DomainRequestApproval;
import com.serpents.ipv6dns.domain.request.DomainRequestResponse;
import com.serpents.ipv6dns.domain.request.DomainRequestsService;
import com.serpents.ipv6dns.spring.user.details.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/request")
public class DomainRequestsEndpoint {

    private final DomainRequestsService service;

    @Autowired
    public DomainRequestsEndpoint(final DomainRequestsService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = POST, consumes = "application/json", produces = "application/json")
    public DomainRequestResponse requestDomain(
            final @AuthenticationPrincipal UserDetailsImpl details,
            final @RequestBody @Valid DomainRequest request) {
        final UUID userId = details.getUserId();
        return service.requestDomain(userId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{requestId}", method = DELETE)
    public void cancelDomainRequest(
            final @AuthenticationPrincipal UserDetailsImpl details,
            final @PathVariable(name = "requestId") UUID requestId) {
        service.cancelRequest(requestId, details.getUserId());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{requestId}/approve", method = POST, consumes = "application/json")
    public void approveDomainRequest(
            final @PathVariable(name = "requestId") UUID requestId,
            final @RequestBody @Valid DomainRequestApproval approval) {
        service.approveRequest(requestId, approval);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{requestId}/reject", method = POST)
    public void rejectDomainRequest(final @PathVariable(name = "requestId") UUID requestId) {
        service.rejectRequest(requestId);
    }
}
