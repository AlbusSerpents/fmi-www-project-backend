package com.serpents.ipv6dns.rest.api;

import com.serpents.ipv6dns.management.ManagementService;
import com.serpents.ipv6dns.user.profile.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/manage")
public class ManagementEndpoint {

    private final ManagementService service;

    @Autowired
    public ManagementEndpoint(final ManagementService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = GET)
    public List<Profile> getAllClients() {
        return service.getAllClients();
    }

    @ResponseStatus(NO_CONTENT)
    @RequestMapping(value = "/{userId}", method = DELETE)
    public void deleteProfile(final @PathVariable(name = "userId") UUID userId) {
        service.deleteProfile(userId);
    }

}
