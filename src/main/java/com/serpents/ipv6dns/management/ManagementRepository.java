package com.serpents.ipv6dns.management;

import java.util.UUID;

public interface ManagementRepository {

    boolean updateIsBlocked(final UUID clientId, final boolean newValue);

    void deleteRequestsByClient(final UUID client);

    void deleteDomainsByOwner(final UUID ownerId);

    boolean deleteClient(final UUID userId);
}
