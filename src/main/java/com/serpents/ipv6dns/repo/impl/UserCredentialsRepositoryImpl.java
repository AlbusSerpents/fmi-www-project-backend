package com.serpents.ipv6dns.repo.impl;

import com.serpents.ipv6dns.credentials.UserCredentials;
import com.serpents.ipv6dns.credentials.UserCredentialsRepository;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.serpents.ipv6dns.utils.JooqField.*;
import static com.serpents.ipv6dns.utils.JooqSchemaUtils.ADMIN_USERS;
import static com.serpents.ipv6dns.utils.JooqSchemaUtils.CLIENT_USERS;
import static org.jooq.impl.DSL.inline;

@Repository
class UserCredentialsRepositoryImpl implements UserCredentialsRepository {

    private final DSLContext context;

    @Autowired
    public UserCredentialsRepositoryImpl(final DSLContext context) {
        this.context = context;
    }

    @Override
    public UserCredentials findAdmin(final String username) {
        return context.select(ADMIN_USERS.getField(ID),
                              ADMIN_USERS.getField(USERNAME),
                              ADMIN_USERS.getField(PASSWORD))
                      .from(ADMIN_USERS.getTable())
                      .where(ADMIN_USERS.getField(USERNAME).equal(inline(username)))
                      .fetchOne(record -> new UserCredentials(record.value1(), record.value2(), record.value3(), false));
    }

    @Override
    public UserCredentials findClient(final String username) {
        return context.select(CLIENT_USERS.getField(ID),
                              CLIENT_USERS.getField(USERNAME),
                              CLIENT_USERS.getField(PASSWORD),
                              CLIENT_USERS.getField(IS_BLOCKED))
                      .from(CLIENT_USERS.getTable())
                      .where(CLIENT_USERS.getField(USERNAME).equal(inline(username)))
                      .fetchOne(record -> new UserCredentials(record.value1(), record.value2(), record.value3(), record.value4()));
    }

}
