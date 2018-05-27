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
public class AdminProfile extends Profile {
    private String email;

    public AdminProfile(final UUID id, final String username, final String name, final String email) {
        super(id, username, name);
        this.email = email;
    }
}
