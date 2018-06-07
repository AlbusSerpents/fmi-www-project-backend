package com.serpents.ipv6dns.address;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Address {
    private final UUID id;
    private final String address;
}
