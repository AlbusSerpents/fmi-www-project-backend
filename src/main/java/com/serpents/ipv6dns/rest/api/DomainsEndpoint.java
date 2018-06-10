package com.serpents.ipv6dns.rest.api;

import com.serpents.ipv6dns.domain.Domain;
import com.serpents.ipv6dns.domain.DomainInfo;
import com.serpents.ipv6dns.domain.DomainService;
import com.serpents.ipv6dns.domain.DomainsSearch;
import com.serpents.ipv6dns.spring.user.details.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.serpents.ipv6dns.utils.UserDetailsUtils.validateUserId;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/domain")
public class DomainsEndpoint {

    private final DomainService service;

    @Autowired
    public DomainsEndpoint(final DomainService service) {
        this.service = service;
    }

    @ResponseStatus(OK)
    @RequestMapping(value = "/view/{domainId}", method = GET, produces = "application/json")
    public DomainInfo getById(final @PathVariable(value = "domainId") UUID domainId) {
        return service.readById(domainId);
    }

    @ResponseStatus(OK)
    @RequestMapping(value = "", method = GET, produces = "application/json")
    public DomainInfo getByCriteria(final @RequestParam(name = "name") String name,
                                    final @RequestParam(name = "address") String address) {
        return service.readByCriteria(name, address);
    }

    @ResponseStatus(OK)
    @RequestMapping(value = "/my-domains/{clientId}", method = GET, produces = "application/json")
    public List<Domain> getMyDomains(
            final @AuthenticationPrincipal UserDetailsImpl details,
            final @PathVariable(value = "clientId") UUID clientId) {
        validateUserId(details, clientId);
        return service.readMy(clientId);
    }

    @ResponseStatus(OK)
    @RequestMapping(value = "/my-domains/{clientId}/{domainId}", method = GET, produces = "application/json")
    public Domain getMyDomain(
            final @AuthenticationPrincipal UserDetailsImpl details,
            final @PathVariable(value = "clientId") UUID clientId,
            final @PathVariable(value = "domainId") UUID domainId) {
        validateUserId(details, clientId);
        return service.readMy(clientId, domainId);
    }

    @ResponseStatus(OK)
    @RequestMapping(value = "/all", method = GET, produces = "application/json")
    public List<Domain> getAllDomains(
            final @Valid DomainsSearch search) {
        return service.readAll(search);
    }

}
