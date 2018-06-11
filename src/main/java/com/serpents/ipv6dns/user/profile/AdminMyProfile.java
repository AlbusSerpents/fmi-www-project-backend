package com.serpents.ipv6dns.user.profile;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AdminMyProfile extends Profile {
    private final String username;

    public AdminMyProfile(final UUID id, final String username, final String name, final String email) {
        super(id, name, email);
        this.username = username;
    }
}
