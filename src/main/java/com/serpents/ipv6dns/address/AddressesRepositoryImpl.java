package com.serpents.ipv6dns.address;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.serpents.ipv6dns.utils.JooqField.ADDRESS;
import static com.serpents.ipv6dns.utils.JooqField.ID;
import static com.serpents.ipv6dns.utils.JooqSchemaUtils.ADDRESSES;
import static com.serpents.ipv6dns.utils.JooqSchemaUtils.FREE_ADDRESSES;
import static org.jooq.impl.DSL.inline;

@Repository
public class AddressesRepositoryImpl implements AddressesRepository {

    private static final RecordMapper<Record2<UUID, String>, Address> ADDRESS_MAPPER =
            record -> new Address(record.value1(), record.value2());

    private DSLContext context;

    @Autowired
    public AddressesRepositoryImpl(final DSLContext context) {
        this.context = context;
    }

    @Override
    public Address findFreeAddress() {
        return context.select(FREE_ADDRESSES.getField(ID), FREE_ADDRESSES.getField(ADDRESS))
                      .from(FREE_ADDRESSES.getTable())
                      .limit(0, 1)
                      .fetchOne(ADDRESS_MAPPER);
    }

    @Override
    public Address findById(final UUID addressId) {
        final Condition idCondition = ADDRESSES.getField(ID).equal(inline(addressId));
        return findAddressByCondition(idCondition);
    }

    @Override
    public Address findByValue(final String address) {
        final Condition addressCondition = ADDRESSES.getField(ADDRESS).equal(inline(address));
        return findAddressByCondition(addressCondition);
    }

    private Address findAddressByCondition(final Condition condition) {
        return context.select(ADDRESSES.getField(ID), ADDRESSES.getField(ADDRESS))
                      .from(ADDRESSES.getTable())
                      .where(condition)
                      .fetchOne(ADDRESS_MAPPER);
    }


}
