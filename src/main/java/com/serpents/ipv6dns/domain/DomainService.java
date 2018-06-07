package com.serpents.ipv6dns.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DomainService {

    private final DomainRepository domainRepository;

    @Autowired
    public DomainService(final DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    public Domain readDomain(final UUID domainId) {
        return domainRepository.findById(domainId);
    }

}
