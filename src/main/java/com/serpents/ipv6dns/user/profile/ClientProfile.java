package com.serpents.ipv6dns.user.profile;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ClientProfile extends Profile {
    private final Integer facultyNumber;

    public ClientProfile(final UUID id, final String name, final String email, final Integer facultyNumber) {
        super(id, name, email);
        this.facultyNumber = facultyNumber;
    }
}
