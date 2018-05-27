package com.serpents.ipv6dns.rest.api;

import com.serpents.ipv6dns.authentication.AuthenticationRequest;
import com.serpents.ipv6dns.authentication.AuthenticationResponse;
import com.serpents.ipv6dns.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth", consumes = "application/json")
public class AuthenticationEndpoint {

    private final AuthenticationService service;

    @Autowired
    public AuthenticationEndpoint(final AuthenticationService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponse login(final HttpSession session, final @RequestBody @Valid AuthenticationRequest request) {
        return service.authenticate(request, session.getId());
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(final HttpSession session, final @AuthenticationPrincipal UserDetails details) {
        System.out.println(details.getUsername());
        session.invalidate();
    }
}
