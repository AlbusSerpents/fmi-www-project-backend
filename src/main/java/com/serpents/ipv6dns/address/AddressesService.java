package com.serpents.ipv6dns.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AddressesService {

    private final AddressesRepository repository;

    @Autowired
    public AddressesService(final AddressesRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<Address> readFreeAddresses() {
        return repository.findAllFree();
    }

    @Transactional
    public Address readById(final UUID id) {
        return repository.findById(id);
    }
}
