package com.serpents.ipv6dns.management;

import com.serpents.ipv6dns.exception.OperationFailedException;
import com.serpents.ipv6dns.user.profile.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ManagementService {

    private final ManagementRepository repository;

    @Autowired
    public ManagementService(final ManagementRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<Profile> getAllClients() {
        return repository.findAllClients();
    }

    @Transactional
    public void deleteProfile(final UUID clientId) {
        final boolean success = repository.deleteClient(clientId);
        if (!success) {
            final String message = String.format("Couldn't delete profile with id: %s", clientId);
            throw new OperationFailedException(message);
        }
    }
}
