package com.serpents.ipv6dns.management;

import com.serpents.ipv6dns.user.profile.Profile;

import java.util.List;
import java.util.UUID;

public interface ManagementRepository {

    List<Profile> findAllClients();

    boolean deleteClient(final UUID userId);
}
