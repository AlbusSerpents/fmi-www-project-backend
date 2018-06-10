package com.serpents.ipv6dns.domain;

import com.serpents.ipv6dns.exception.InvalidSearchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DomainService {

    private final DomainRepository repository;

    @Autowired
    public DomainService(final DomainRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public DomainInfo readById(final UUID domainId) {
        return repository.findById(domainId);
    }

    @Transactional
    public DomainInfo readByCriteria(final String name, final String address) {
        if (name == null && address == null) {
            throw new InvalidSearchException("Fill either the name or the address field");
        }

        return repository.findByCriteria(name, address);
    }

    @Transactional
    public List<Domain> readMy(final UUID clientId) {
        return repository.findByOwner(clientId);
    }

    @Transactional
    public Domain readMy(final UUID clientId, final UUID domainId) {
        return repository.findByOwnerAndId(clientId, domainId);
    }

    @Transactional
    public List<Domain> readAll(final DomainsSearch search) {
        return repository.findAll(search);
    }

}
