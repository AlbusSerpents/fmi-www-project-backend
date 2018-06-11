package com.serpents.ipv6dns.management;

import com.serpents.ipv6dns.exception.OperationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ManagementService {

    private final ManagementRepository repository;

    @Autowired
    public ManagementService(final ManagementRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void block(final UUID clientId) {
        updateIsBlocked(clientId, true);
    }

    @Transactional
    public void unblocked(final UUID clientId) {
        updateIsBlocked(clientId, false);
    }

    private void updateIsBlocked(final UUID clientId, final boolean newValue) {
        if (!repository.updateIsBlocked(clientId, newValue)) {
            final String message = String.format("Couldn't block client: %S", clientId);
            throw new OperationFailedException(message);
        }
    }

    @Transactional
    public void revokeDomainOwnership(final UUID clientId) {
        repository.deleteRequestsByClient(clientId);
        repository.deleteDomainsByOwner(clientId);
    }

    @Transactional
    public void deleteProfile(final UUID clientId) {
        revokeDomainOwnership(clientId);
        final boolean success = repository.deleteClient(clientId);
        if (!success) {
            final String message = String.format("Couldn't delete profile with id: %s", clientId);
            throw new OperationFailedException(message);
        }
    }
}
