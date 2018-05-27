package com.serpents.ipv6dns.user.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RegistrationService {

    private final RegistrationRepository repository;
    private final PasswordEncoder encoder;

    @Autowired
    public RegistrationService(final RegistrationRepository repository, final PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Transactional
    public RegistrationResponse register(final RegistrationRequest request) {
        final String rawPassword = request.getPassword();
        final String encodedPassword = encoder.encode(rawPassword);
        request.setPassword(encodedPassword);

        final UUID id = repository.createClient(request);
        return new RegistrationResponse(id);
    }
}
