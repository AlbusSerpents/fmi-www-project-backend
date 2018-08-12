package com.serpents.ipv6dns.user.registration;

import com.serpents.ipv6dns.authentication.AuthenticationRequest;
import com.serpents.ipv6dns.authentication.AuthenticationResponse;
import com.serpents.ipv6dns.authentication.AuthenticationService;
import com.serpents.ipv6dns.exception.OperationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.serpents.ipv6dns.credentials.UserRole.CLIENT;

@Service
public class RegistrationService {

    private final RegistrationRepository repository;
    private final PasswordEncoder encoder;
    private final AuthenticationService auth;

    @Autowired
    public RegistrationService(
            final RegistrationRepository repository,
            final PasswordEncoder encoder,
            final AuthenticationService auth) {
        this.repository = repository;
        this.encoder = encoder;
        this.auth = auth;
    }

    @Transactional
    public AuthenticationResponse register(final RegistrationRequest request, final String sessionId) {
        final String rawPassword = request.getPassword();
        final String encodedPassword = encoder.encode(rawPassword);
        request.setPassword(encodedPassword);

        final Optional<UUID> id = repository.createClient(request);
        if (id.isPresent()) {
            final AuthenticationRequest authRequest = buildAuthenticationRequest(request.getUsername(), rawPassword);
            return auth.authenticate(authRequest, sessionId);
        } else {
            throw new OperationFailedException("Couldn't complete registration");
        }
    }

    private AuthenticationRequest buildAuthenticationRequest(final String username, final String password) {
        return new AuthenticationRequest(username, password, CLIENT.getToken());
    }
}
