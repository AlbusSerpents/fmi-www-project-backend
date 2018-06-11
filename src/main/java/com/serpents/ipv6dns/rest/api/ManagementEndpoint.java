package com.serpents.ipv6dns.rest.api;

import com.serpents.ipv6dns.management.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/manage")
public class ManagementEndpoint {

    private final ManagementService service;

    @Autowired
    public ManagementEndpoint(final ManagementService service) {
        this.service = service;
    }

    @ResponseStatus(NO_CONTENT)
    @RequestMapping(value = "/{userId}/block", method = POST)
    public void block(final @PathVariable(value = "userId") UUID clientId) {
        service.block(clientId);
    }

    @ResponseStatus(NO_CONTENT)
    @RequestMapping(value = "/{userId}/unblock", method = POST)
    public void unblock(final @PathVariable(value = "userId") UUID clientId) {
        service.unblocked(clientId);
    }

    @ResponseStatus(NO_CONTENT)
    @RequestMapping(value = "/{userId}/domains", method = DELETE)
    public void deleteDomains(final @PathVariable(value = "userId") UUID clientId) {
        service.revokeDomainOwnership(clientId);
    }

    @ResponseStatus(NO_CONTENT)
    @RequestMapping(value = "/{userId}", method = DELETE)
    public void deleteProfile(final @PathVariable(name = "userId") UUID userId) {
        service.deleteProfile(userId);
    }

}
