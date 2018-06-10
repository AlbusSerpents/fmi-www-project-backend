package com.serpents.ipv6dns.repo.impl;

import com.serpents.ipv6dns.user.registration.RegistrationRepository;
import com.serpents.ipv6dns.user.registration.RegistrationRequest;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.jooq.impl.DSL.*;

@Repository
public class RegistrationRepositoryImpl implements RegistrationRepository {

    private final DSLContext context;

    @Autowired
    public RegistrationRepositoryImpl(final DSLContext context) {
        this.context = context;
    }

    @Override
    public UUID createClient(final RegistrationRequest request) {
        final UUID id = randomUUID();

        final Table<?> usersTable = table("users");
        final Table<?> clientsTable = table("clients");
        final Field<UUID> idField = field("id", UUID.class);

        final Field<String> usernameField = field("username", String.class);
        final Field<String> passwordField = field("password", String.class);
        final Field<String> nameField = field("name", String.class);

        final Field<String> emailField = field("email", String.class);
        final Field<Integer> facultyNumberField = field("faculty_number", Integer.class);
        final Field<Boolean> isBlockedField = field("is_blocked", Boolean.class);

        context.insertInto(usersTable, idField, usernameField, passwordField, nameField)
               .values(inline(id), inline(request.getUsername()), inline(request.getPassword()), inline(request.getName()))
               .execute();
        context.insertInto(clientsTable, idField, emailField, facultyNumberField, isBlockedField)
               .values(inline(id), inline(request.getEmail()), inline(request.getFacultyNumber()), inline(false))
               .execute();

        return id;
    }
}
