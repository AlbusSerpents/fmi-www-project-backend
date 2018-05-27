package com.serpents.ipv6dns.user.profile;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ClientProfile extends Profile {
    private Integer facultyNumber;
    private String email;

    public ClientProfile(final UUID id, final String username, final String name, final Integer facultyNumber, final String email) {
        super(id, username, name);
        this.facultyNumber = facultyNumber;
        this.email = email;
    }
}
