package com.serpents.ipv6dns.repo.impl;

import com.serpents.ipv6dns.user.registration.RegistrationRepository;
import com.serpents.ipv6dns.user.registration.RegistrationRequest;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.serpents.ipv6dns.utils.JooqField.*;
import static com.serpents.ipv6dns.utils.JooqSchemaUtils.CLIENTS;
import static com.serpents.ipv6dns.utils.JooqSchemaUtils.USERS;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static org.jooq.impl.DSL.inline;

@Repository
public class RegistrationRepositoryImpl implements RegistrationRepository {

    private final DSLContext context;

    @Autowired
    public RegistrationRepositoryImpl(final DSLContext context) {
        this.context = context;
    }

    @Override
    public Optional<UUID> createClient(final RegistrationRequest request) {
        final UUID id = randomUUID();

        final boolean inserted =
                context.insertInto(USERS.getTable(), USERS.getField(ID), USERS.getField(USERNAME), USERS.getField(PASSWORD), USERS.getField(NAME))
                       .values(inline(id), inline(request.getUsername()), inline(request.getPassword()), inline(request.getName()))
                       .execute() == 1
                &&
                context.insertInto(CLIENTS.getTable(), CLIENTS.getField(ID), CLIENTS.getField(EMAIL), CLIENTS.getField(FACULTY_NUMBER), CLIENTS.getField(IS_BLOCKED))
                       .values(inline(id), inline(request.getEmail()), inline(request.getFacultyNumber()), inline(false))
                       .execute() == 1;

        return inserted ? of(id) : empty();
    }
}
