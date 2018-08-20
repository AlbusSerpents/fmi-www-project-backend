package com.serpents.ipv6dns.authentication;

import com.serpents.ipv6dns.spring.user.details.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse authenticate(final AuthenticationRequest request, final String sessionId) {
        final char token = request.getRoleToken();
        final String username = request.getUsername();
        final String authenticationUsername = token + username;
        final String password = request.getPassword();
        final Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(authenticationUsername, password);

        final Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authenticationResponse);
        final UserDetailsImpl details = (UserDetailsImpl) authenticationResponse.getPrincipal();

        return new AuthenticationResponse(sessionId, details.getUserId(), username);
    }
}
