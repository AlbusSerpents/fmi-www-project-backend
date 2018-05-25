package com.serpents.ipv6dns.repository;

import com.serpents.ipv6dns.model.UserCredentials;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static org.jooq.impl.DSL.*;

@Repository
public class AuthenticationRepository {

    private final DSLContext context;

    @Autowired
    public AuthenticationRepository(final DSLContext context) {
        this.context = context;
    }

    public UserCredentials findAdmin(final String username, final String password) {
        final Table<?> adminsTable = table("admin_users");
        final Field<String> usernameField = field("username", String.class);
        final Field<String> passwordField = field("password", String.class);


        final Select<Record2<String, String>> select =
                context.select(usernameField, passwordField)
                       .from(adminsTable)
                       .where(usernameField.equal(inline(username))
                                           .and(passwordField.equal(inline(password))));
        return select.fetchOne(record -> new UserCredentials(record.value1(), record.value2()));
    }
}
