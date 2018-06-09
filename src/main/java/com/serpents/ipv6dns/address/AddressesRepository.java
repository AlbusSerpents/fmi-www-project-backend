package com.serpents.ipv6dns.address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressesRepository {

    List<Address> findAllFree();

    Optional<Address> findFree();

    Address findById(final UUID addressId);
}
