package com.serpents.ipv6dns.utils;

import java.time.LocalDateTime;
import java.util.UUID;

public class JooqField<T> {
    public static final JooqField<UUID> ID = new JooqField<>(UUID.class);
    public static final JooqField<String> USERNAME = new JooqField<>(String.class);
    public static final JooqField<String> PASSWORD = new JooqField<>(String.class);
    public static final JooqField<String> NAME = new JooqField<>(String.class);
    public static final JooqField<String> EMAIL = new JooqField<>(String.class);
    public static final JooqField<Integer> FACULTY_NUMBER = new JooqField<>(Integer.class);
    public static final JooqField<Boolean> IS_BLOCKED = new JooqField<>(Boolean.class);
    public static final JooqField<String> DOMAIN_NAME = new JooqField<>(String.class);
    public static final JooqField<String> DESCRIPTION = new JooqField<>(String.class);
    public static final JooqField<UUID> CLIENT_ID = new JooqField<>(UUID.class);
    public static final JooqField<LocalDateTime> UPDATED_AT = new JooqField<>(LocalDateTime.class);
    public static final JooqField<String> STATUS = new JooqField<>(String.class);
    public static final JooqField<UUID> DETAILS_ID = new JooqField<>(UUID.class);
    public static final JooqField<String> ADDRESS = new JooqField<>(String.class);
    public static final JooqField<UUID> OWNER = new JooqField<>(UUID.class);


    private JooqField(final Class<T> fieldClass) {
        this.fieldClass = fieldClass;
    }

    private final Class<T> fieldClass;

    public Class<T> getFieldClass() {
        return fieldClass;
    }
}