package com.serpents.ipv6dns.repo.impl;

import com.serpents.ipv6dns.address.Address;
import com.serpents.ipv6dns.domain.Domain;
import com.serpents.ipv6dns.domain.Domain.DomainInfo;
import com.serpents.ipv6dns.domain.DomainDetails;
import com.serpents.ipv6dns.domain.DomainRepository;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.serpents.ipv6dns.utils.JooqField.*;
import static com.serpents.ipv6dns.utils.JooqTable.*;
import static com.serpents.ipv6dns.utils.JooqUtils.optionalFieldCondition;
import static org.jooq.impl.DSL.inline;

@Repository
public class DomainRepositoryImpl implements DomainRepository {

    private static final RecordMapper<Record2<String, String>, DomainInfo> INFO_MAPPER = record -> new DomainInfo(record.value1(), record.value2());

    private static final RecordMapper<Record7<UUID, UUID, UUID, String, UUID, String, String>, Domain> MAPPER = record -> {
        final Address address = new Address(record.value3(), record.value4());
        final DomainDetails domainDetails = new DomainDetails(record.value5(), record.value6(), record.value7());
        return new Domain(record.value1(), record.value2(), domainDetails, address);
    };

    private final DSLContext context;


    @Autowired
    public DomainRepositoryImpl(final DSLContext context) {
        this.context = context;
    }

    @Override
    public DomainInfo findByCriteria(final String name, final String address) {
        return findInfoByCondition(nameCondition(name).and(addressCondition(address)));
    }

    @Override
    public List<Domain> findByOwner(final UUID ownerId) {
        return selectByCondition(ownerCondition(ownerId)).fetch(MAPPER);
    }

    @Override
    public void insert(final Domain domain) {
        context
                .insertInto(DOMAINS.getTable(), DOMAINS.getField(ID), DOMAINS.getField(OWNER), DOMAINS.getField(DETAILS_ID))
                .values(inline(domain.getAddress().getId()), inline(domain.getOwner()), inline(domain.getDomainDetails().getId()))
                .returning(DOMAINS.getField(ID))
                .fetchOne()
                .get(DOMAINS.getField(ID));
    }

    private SelectConditionStep<Record7<UUID, UUID, UUID, String, UUID, String, String>> selectByCondition(final Condition condition) {
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
                      .where(condition);
    }

    private DomainInfo findInfoByCondition(final Condition condition) {
        return context.select(ADDRESSES.getField(ADDRESS),
                              DOMAIN_DETAILS.getField(DOMAIN_NAME))
                      .from(DOMAINS.getTable())
                      .join(ADDRESSES.getTable()).on(ADDRESSES.getField(ID).equal(DOMAINS.getField(ID)))
                      .join(DOMAIN_DETAILS.getTable()).on(DOMAIN_DETAILS.getField(ID).equal(DOMAINS.getField(DETAILS_ID)))
                      .where(condition)
                      .fetchOne(INFO_MAPPER);
    }

    private static Condition ownerCondition(final UUID ownerId) {
        return DOMAINS.getField(OWNER).equal(inline(ownerId));
    }

    private static Condition addressCondition(final String address) {
        return optionalFieldCondition(ADDRESSES.getField(ADDRESS), address);
    }

    private static Condition nameCondition(final String name) {
        return optionalFieldCondition(DOMAIN_DETAILS.getField(DOMAIN_NAME), name);
    }
}
