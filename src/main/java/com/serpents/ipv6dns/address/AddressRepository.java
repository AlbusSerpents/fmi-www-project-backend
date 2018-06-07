package com.serpents.ipv6dns.address;

import java.util.UUID;

public interface AddressRepository {

    Address findFreeAddress();

    Address findById(final UUID id);
}
