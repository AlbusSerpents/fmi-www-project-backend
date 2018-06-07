package com.serpents.ipv6dns.utils;

import com.serpents.ipv6dns.domain.request.DomainRequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class JooqField<T> {
    public static final JooqField<UUID> ID = new JooqField<>();
    public static final JooqField<String> DOMAIN_NAME = new JooqField<>();
    public static final JooqField<String> DESCRIPTION = new JooqField<>();
    public static final JooqField<UUID> CLIENT_ID = new JooqField<>();
    public static final JooqField<LocalDateTime> UPDATED_AT = new JooqField<>();
    public static final JooqField<DomainRequestStatus> STATUS = new JooqField<>();
    public static final JooqField<UUID> DETAILS_ID = new JooqField<>();
    public static final JooqField<String> ADDRESS = new JooqField<>();

    private JooqField() {
    }
}