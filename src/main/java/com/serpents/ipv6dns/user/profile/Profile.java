package com.serpents.ipv6dns.user.profile;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Profile {
    private final UUID id;
    private final String name;
    private final String email;
    private final Integer facultyNumber;
}
