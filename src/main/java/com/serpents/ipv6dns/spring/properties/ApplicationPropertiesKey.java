package com.serpents.ipv6dns.spring.properties;

public enum ApplicationPropertiesKey {
    APPLICATION_NAME("application.name"),
    REALM_NAME("security.realm.name"),
    SESSION_INACTIVITY_INTERVAL_IN_SECONDS("session.timeout.seconds"),
    JDBC_URL("database.url"),
    DNS_ZONE_NAME("dsn.zone.name");

    ApplicationPropertiesKey(final String key) {
        this.key = key;
    }

    private final String key;

    public String getKey() {
        return key;
    }
}
