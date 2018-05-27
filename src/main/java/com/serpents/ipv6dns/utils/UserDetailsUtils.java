package com.serpents.ipv6dns.utils;

import com.serpents.ipv6dns.exception.BadRequestException;
import com.serpents.ipv6dns.spring.user.details.UserDetailsImpl;

import java.util.UUID;

public class UserDetailsUtils {

    private UserDetailsUtils() {

    }

    public static void validateUserId(final UserDetailsImpl details, final UUID userId) {
        final UUID detailsId = details.getUserId();

        if (!detailsId.equals(userId)) {
            throw new BadRequestException();
        }
    }

}
