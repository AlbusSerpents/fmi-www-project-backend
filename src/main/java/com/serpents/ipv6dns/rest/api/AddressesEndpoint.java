package com.serpents.ipv6dns.rest.api;

import com.serpents.ipv6dns.address.Address;
import com.serpents.ipv6dns.address.AddressesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/addresses")
public class AddressesEndpoint {

    private final AddressesService service;

    @Autowired
    public AddressesEndpoint(final AddressesService service) {
        this.service = service;
    }

    @RequestMapping(value = "", method = GET, produces = "application/json")
    public List<Address> getAllFree() {
        return service.readFreeAddresses();
    }

    @RequestMapping(value = "/{addressId}", method = GET, produces = "application/json")
    public Address getById(final @PathVariable("addressId") UUID addressId) {
        return service.readById(addressId);
    }

}
