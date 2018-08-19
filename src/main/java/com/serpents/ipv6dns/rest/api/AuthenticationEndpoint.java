package com.serpents.ipv6dns.rest.api;

import com.serpents.ipv6dns.authentication.AuthenticationRequest;
import com.serpents.ipv6dns.authentication.AuthenticationResponse;
import com.serpents.ipv6dns.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationEndpoint {

    private final AuthenticationService service;

    @Autowired
    public AuthenticationEndpoint(final AuthenticationService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = POST, consumes = "application/json", produces = "application/json")
    @ResponseStatus(CREATED)
    public AuthenticationResponse login(
            final HttpSession session,
            final @RequestBody @Valid AuthenticationRequest request) {
        return service.authenticate(request, session.getId());
    }

    @RequestMapping(value = "", method = DELETE)
    @ResponseStatus(NO_CONTENT)
    public void logout(
            final HttpSession session,
            final @AuthenticationPrincipal UserDetails details) {
        session.invalidate();
    }
}
