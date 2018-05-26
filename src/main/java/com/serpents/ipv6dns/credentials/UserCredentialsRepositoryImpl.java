package com.serpents.ipv6dns.credentials;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static org.jooq.impl.DSL.*;

@Repository
class UserCredentialsRepositoryImpl implements UserCredentialsRepository {

    private final DSLContext context;

    @Autowired
    public UserCredentialsRepositoryImpl(final DSLContext context) {
        this.context = context;
    }

    @Override
    public UserCredentials findAdmin(final String username) {
        final Table<?> adminsTable = table("admin_users");
        final Param<Boolean> isBlockedField = inline(true);

        return findFromUserTableByUsername(adminsTable, username, isBlockedField);
    }

    @Override
    public UserCredentials findClient(final String username) {
        final Table<?> clientsTable = table("client_users");
        final Field<Boolean> isBlockedField = field("is_blocked", Boolean.class);

        return findFromUserTableByUsername(clientsTable, username, isBlockedField);
    }

    private UserCredentials findFromUserTableByUsername(final Table<?> table, final String username, final Field<Boolean> isBlockedField) {
        final Field<UUID> userIdField = field("id", UUID.class);
        final Field<String> usernameField = field("username", String.class);
        final Field<String> passwordField = field("password", String.class);

        return context.select(userIdField, usernameField, passwordField, isBlockedField)
                      .from(table)
                      .where(usernameField.equal(inline(username)))
                      .fetchOne(record -> new UserCredentials(record.value1(), record.value2(), record.value3(), record.value4()));
    }
}
