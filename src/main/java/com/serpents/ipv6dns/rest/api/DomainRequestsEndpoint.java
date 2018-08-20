package com.serpents.ipv6dns.rest.api;

import com.serpents.ipv6dns.domain.DomainDetails;
import com.serpents.ipv6dns.domain.request.DomainRequestInfo;
import com.serpents.ipv6dns.domain.request.DomainRequestsService;
import com.serpents.ipv6dns.spring.user.details.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
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
    public void requestDomain(
            final @AuthenticationPrincipal UserDetailsImpl details,
            final @RequestBody @Valid DomainDetails domainDetails) {
        final UUID userId = details.getUserId();
        service.requestDomain(userId, domainDetails);
    }

    @ResponseStatus(OK)
    @RequestMapping(value = "", method = GET, produces = "application/json")
    public List<DomainRequestInfo> getPending() {
        return service.listPending();
    }

    @ResponseStatus(CREATED)
    @RequestMapping(value = "/{requestId}/approve", method = POST, consumes = "application/json")
    public void approve(final @PathVariable(name = "requestId") UUID requestId) {
        service.approve(requestId);
    }

    @ResponseStatus(NO_CONTENT)
    @RequestMapping(value = "/{requestId}/reject", method = POST)
    public void reject(final @PathVariable(name = "requestId") UUID requestId) {
        service.rejectRequest(requestId);
    }
}
