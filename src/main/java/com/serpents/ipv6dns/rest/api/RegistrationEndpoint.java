package com.serpents.ipv6dns.rest.api;

import com.serpents.ipv6dns.user.registration.RegistrationRequest;
import com.serpents.ipv6dns.user.registration.RegistrationResponse;
import com.serpents.ipv6dns.user.registration.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/client", consumes = "application/json")
public class RegistrationEndpoint {

    private final RegistrationService service;

    @Autowired
    public RegistrationEndpoint(final RegistrationService service) {
        this.service = service;
    }

    @ResponseStatus(CREATED)
    @RequestMapping(value = "", method = POST)
    public RegistrationResponse register(final @RequestBody @Valid RegistrationRequest request) {
        return service.register(request);
    }
}
