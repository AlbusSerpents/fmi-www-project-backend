package com.serpents.ipv6dns.credentials;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserCredentials {
    private final UUID userId;
    private final String username;
    private final String password;

}
