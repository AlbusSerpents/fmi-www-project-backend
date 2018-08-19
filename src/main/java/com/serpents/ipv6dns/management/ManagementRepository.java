package com.serpents.ipv6dns.management;

import com.serpents.ipv6dns.user.profile.ClientProfile;

import java.util.List;
import java.util.UUID;

public interface ManagementRepository {

    List<ClientProfile> findAllClients();

    boolean updateIsBlocked(final UUID clientId, final boolean newValue);

    void deleteRequestsByClient(final UUID client);

    void deleteDomainsByOwner(final UUID ownerId);

    boolean deleteClient(final UUID userId);
}
