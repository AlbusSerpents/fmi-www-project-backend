package com.serpents.ipv6dns.user.registration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;
import java.util.function.Supplier;

import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegistrationServiceTest {

    private RegistrationService service;
    private RegistrationRepository repository;
    private PasswordEncoder encoder;

    @Before
    public void setUp() {
        repository = mock(RegistrationRepository.class);
        encoder = mock(PasswordEncoder.class);

        service = new RegistrationService(repository, encoder);
    }

    @Test
    public void register_ValidateWorkflow() {
        final RegistrationRequest request = new RegistrationRequest();
        request.setPassword("Password");

        mockEncodePassword(Matchers::any, "encoded password");
        mockCreateClient(randomUUID());

        assertNotNull(service.register(request));
    }

    @Test
    public void register() {
        final UUID id = randomUUID();
        final String password = "password";
        final RegistrationRequest request = new RegistrationRequest();
        request.setPassword(password);

        mockEncodePassword(() -> eq(password), "encoded");
        mockCreateClient(id);

        final RegistrationResponse actual = service.register(request);
        final RegistrationResponse expected = new RegistrationResponse(id);

        assertEquals(expected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void register_CreateFailed() {
        final String password = "some password";
        final RegistrationRequest request = new RegistrationRequest();
        request.setPassword(password);

        mockEncodePassword(() -> eq(password), "encoded");
        when(repository.createClient(any())).thenThrow(new RuntimeException());

        service.register(request);
    }

    private void mockEncodePassword(final Supplier<String> expected, final String result) {
        when(encoder.encode(expected.get())).thenReturn(result);
    }

    private void mockCreateClient(final UUID result) {
        when(repository.createClient(any())).thenReturn(of(result));
    }
}