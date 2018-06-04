package com.serpents.ipv6dns.utils;

import com.serpents.ipv6dns.spring.user.details.UserDetailsImpl;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.UUID;

public class UserDetailsUtils {

    private UserDetailsUtils() {

    }

    public static void validateUserId(final UserDetailsImpl details, final UUID userId) {
        if (details == null || userId == null) {
            throw new BadCredentialsException("Unauthorized");
        }
        final UUID detailsId = details.getUserId();
        if (!userId.equals(detailsId)) {
            throw new BadCredentialsException("Unauthorized");
        }
    }

}
