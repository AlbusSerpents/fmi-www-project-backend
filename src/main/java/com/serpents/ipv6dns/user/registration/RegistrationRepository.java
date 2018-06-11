package com.serpents.ipv6dns.user.registration;

import java.util.Optional;
import java.util.UUID;

public interface RegistrationRepository {

    Optional<UUID> createClient(final RegistrationRequest request);

}
