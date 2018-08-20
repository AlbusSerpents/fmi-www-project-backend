package com.serpents.ipv6dns.repo.impl;

import com.serpents.ipv6dns.address.Address;
import com.serpents.ipv6dns.address.AddressesRepository;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.RecordMapper;
import org.jooq.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.serpents.ipv6dns.utils.JooqField.ADDRESS;
import static com.serpents.ipv6dns.utils.JooqField.ID;
import static com.serpents.ipv6dns.utils.JooqTable.FREE_ADDRESSES;
import static java.util.Optional.ofNullable;

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

    private Select<Record2<UUID, String>> selectAllFreeAddresses() {
        return context.select(FREE_ADDRESSES.getField(ID),
                              FREE_ADDRESSES.getField(ADDRESS))
                      .from(FREE_ADDRESSES.getTable());
    }

}
