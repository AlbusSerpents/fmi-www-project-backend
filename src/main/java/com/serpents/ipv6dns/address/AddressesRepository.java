package com.serpents.ipv6dns.address;

import java.util.UUID;

public interface AddressesRepository {

    Address findFreeAddress();

    Address findById(final UUID addressId);

    Address findByValue(final String address);
}
