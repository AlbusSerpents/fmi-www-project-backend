package com.serpents.ipv6dns.utils;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static java.time.ZoneId.of;

public class TimeUtils {

    private TimeUtils() {
    }

    public static LocalDateTime nowAtUtc() {
        return now(of("UTC"));
    }
}
