package com.serpents.ipv6dns.address;

import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.serpents.ipv6dns.utils.JooqField.ADDRESS;
import static com.serpents.ipv6dns.utils.JooqField.ID;
import static com.serpents.ipv6dns.utils.JooqSchemaUtils.ADDRESSES;
import static com.serpents.ipv6dns.utils.JooqSchemaUtils.FREE_ADDRESSES;
import static java.util.Optional.ofNullable;
import static org.jooq.impl.DSL.inline;

@Repository
public class AddressesRepositoryImpl implements AddressesRepository {

    private static final RecordMapper<Record2<UUID, String>, Address> ADDRESS_MAPPER =
            record -> new Address(record.value1(), record.value2());

    private final DSLContext context;

    @Autowired
    public AddressesRepositoryImpl(final DSLContext context) {
        this.context = context;
    }

    @Override
    public Optional<Address> findFree() {
        return ofNullable(selectAllFreeAddresses().fetchAny(ADDRESS_MAPPER));
    }

    @Override
    public Address findById(final UUID addressId) {
        final Condition idCondition = ADDRESSES.getField(ID).equal(inline(addressId));
        return context.select(ADDRESSES.getField(ID), ADDRESSES.getField(ADDRESS))
                      .from(ADDRESSES.getTable())
                      .where(idCondition)
                      .fetchOne(ADDRESS_MAPPER);
    }

    @Override
    public List<Address> findAllFree() {
        return selectAllFreeAddresses().fetch(ADDRESS_MAPPER);
    }

    private Select<Record2<UUID, String>> selectAllFreeAddresses() {
        return context.select(FREE_ADDRESSES.getField(ID),
                              FREE_ADDRESSES.getField(ADDRESS))
                      .from(FREE_ADDRESSES.getTable());
    }

}
