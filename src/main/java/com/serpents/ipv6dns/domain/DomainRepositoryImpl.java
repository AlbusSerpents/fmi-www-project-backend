package com.serpents.ipv6dns.domain;

import com.serpents.ipv6dns.address.Address;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.serpents.ipv6dns.utils.JooqField.*;
import static com.serpents.ipv6dns.utils.JooqSchemaUtils.*;
import static org.jooq.impl.DSL.inline;

@Repository
public class DomainRepositoryImpl implements DomainRepository {

    private DSLContext context;

    @Autowired
    public DomainRepositoryImpl(final DSLContext context) {
        this.context = context;
    }

    @Override
    public Domain findById(final UUID domainId) {
        return context.select(DOMAINS.getField(ID),
                              DOMAINS.getField(OWNER),
                              ADDRESSES.getField(ID),
                              ADDRESSES.getField(ADDRESS),
                              DOMAIN_DETAILS.getField(ID),
                              DOMAIN_DETAILS.getField(DOMAIN_NAME),
                              DOMAIN_DETAILS.getField(DESCRIPTION))
                      .from(DOMAINS.getTable())
                      .join(ADDRESSES.getTable()).on(ADDRESSES.getField(ID).equal(DOMAINS.getField(ID)))
                      .join(DOMAIN_DETAILS.getTable()).on(DOMAIN_DETAILS.getField(ID).equal(DOMAINS.getField(DETAILS_ID)))
                      .where(DOMAINS.getField(ID).equal(inline(domainId)))
                      .fetchOne(record -> {
                          final Address address = new Address(record.value3(), record.value4());
                          final DomainDetails domainDetails = new DomainDetails(record.value5(), record.value6(), record.value7());
                          return new Domain(record.value1(), record.value2(), domainDetails, address);
                      });
    }

    @Override
    public DomainCreatedResponse insert(final Domain domain) {
        final UUID id = context
                .insertInto(DOMAINS.getTable(), DOMAINS.getField(ID), DOMAINS.getField(OWNER), DOMAINS.getField(DETAILS_ID))
                .values(inline(domain.getAddress().getId()), inline(domain.getOwner()), inline(domain.getDomainDetails().getId()))
                .returning(DOMAINS.getField(ID))
                .fetchOne()
                .get(DOMAINS.getField(ID));

        return new DomainCreatedResponse(id);
    }
}
