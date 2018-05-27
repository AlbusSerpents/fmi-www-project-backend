package com.serpents.ipv6dns.user.registration;

import java.util.UUID;

public interface RegistrationRepository {

    UUID createClient(final RegistrationRequest request);

}
