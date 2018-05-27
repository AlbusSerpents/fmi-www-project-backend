package com.serpents.ipv6dns.user.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    private UUID id;
    private String username;
    private String name;
}
