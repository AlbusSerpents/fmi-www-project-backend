package com.serpents.ipv6dns.rest.api;

import com.serpents.ipv6dns.domain.DomainCreatedResponse;
import com.serpents.ipv6dns.domain.DomainDetails;
import com.serpents.ipv6dns.domain.request.DomainRequestApproval;
import com.serpents.ipv6dns.domain.request.DomainRequestResponse;
import com.serpents.ipv6dns.domain.request.DomainRequestsService;
import com.serpents.ipv6dns.spring.user.details.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
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

    @ResponseStatus(CREATED)
    @RequestMapping(value = "", method = POST, consumes = "application/json", produces = "application/json")
    public DomainRequestResponse requestDomain(
            final @AuthenticationPrincipal UserDetailsImpl userDetails,
            final @RequestBody @Valid DomainDetails domainDetails) {
        final UUID userId = userDetails.getUserId();
        return service.requestDomain(userId, domainDetails);
    }

    @ResponseStatus(NO_CONTENT)
    @RequestMapping(value = "/{requestId}", method = DELETE)
    public void cancelDomainRequest(
            final @AuthenticationPrincipal UserDetailsImpl details,
            final @PathVariable(name = "requestId") UUID requestId) {
        service.cancelRequest(requestId, details.getUserId());
    }

    @ResponseStatus(CREATED)
    @RequestMapping(value = "/{requestId}/approve", method = POST, consumes = "application/json")
    public DomainCreatedResponse approveDomainRequest(
            final @PathVariable(name = "requestId") UUID requestId,
            final @RequestBody @Valid DomainRequestApproval approval) {
        return service.approveRequest(requestId, approval);
    }

    @ResponseStatus(NO_CONTENT)
    @RequestMapping(value = "/{requestId}/reject", method = POST)
    public void rejectDomainRequest(final @PathVariable(name = "requestId") UUID requestId) {
        service.rejectRequest(requestId);
    }
}
