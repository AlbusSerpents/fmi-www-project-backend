package com.serpents.ipv6dns.user.profile;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MyProfile extends Profile {
    private final String username;

    public MyProfile(
            final UUID id,
            final String username,
            final String name,
            final Integer facultyNumber,
            final String email) {
        super(id, name, email, facultyNumber);
        this.username = username;
    }
}
