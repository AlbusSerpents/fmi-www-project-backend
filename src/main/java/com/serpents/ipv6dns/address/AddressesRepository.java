package com.serpents.ipv6dns.address;

import java.util.Optional;

public interface AddressesRepository {

    Optional<Address> findFree();
}
