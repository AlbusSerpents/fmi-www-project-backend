package com.serpents.ipv6dns.user.profile;

import com.serpents.ipv6dns.credentials.UserRole;
import com.serpents.ipv6dns.user.profile.ProfileUpdateRequest.ChangePasswordRequest;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static org.jooq.impl.DSL.*;

@Repository
public class ProfileRepositoryImpl implements ProfileRepository {

    private final Field<UUID> idField = field("id", UUID.class);
    private final Field<String> usernameField = field("username", String.class);
    private final Field<String> nameField = field("name", String.class);
    private final Field<String> emailField = field("email", String.class);

    private DSLContext context;

    @Autowired
    public ProfileRepositoryImpl(final DSLContext context) {
        this.context = context;
    }

    @Override
    public ClientProfile findClient(final UUID id) {
        final Table<?> clientsTable = table("client_users");

        final Field<Integer> facultyNumberField = field("faculty_number", Integer.class);

        return context.select(idField, usernameField, nameField, facultyNumberField, emailField)
                      .from(clientsTable)
                      .where(idField.equal(inline(id)))
                      .fetchOne(record -> new ClientProfile(record.value1(), record.value2(), record.value3(), record.value4(), record.value5()));
    }

    @Override
    public AdminProfile findAdmin(final UUID id) {
        final Table<?> adminsTable = table("admins_table");

        return context.select(idField, usernameField, nameField, emailField)
                      .from(adminsTable)
                      .where(idField.equal(inline(id)))
                      .fetchOne(record -> new AdminProfile(record.value1(), record.value2(), record.value3(), record.value4()));
    }

    @Override
    public void updateClientEmail(final UUID id, final String email) {
        final Table<?> adminTable = table("clients");
        updateEmailFieldForTable(adminTable, id, email);
    }

    @Override
    public void updateAdminEmail(final UUID id, final String email) {
        final Table<?> adminTable = table("admins");
        updateEmailFieldForTable(adminTable, id, email);
    }

    private void updateEmailFieldForTable(final Table<?> table, final UUID id, final String email) {

        final Field<UUID> idField = field("id", UUID.class);
        final Field<String> emailField = field("email", String.class);

        context.update(table)
               .set(row(emailField), row(inline(email)))
               .where(idField.equal(inline(id)))
               .execute();
    }

    @Override
    public void changePassword(final UUID userId, final UserRole role, final ChangePasswordRequest request) {
        final Table<?> usersTable = table("users");
        final Table<?> extraUsersTable = table(extraUsersTableForRole(role));


        final Field<UUID> idField = field("id", UUID.class);
        final Field<String> passwordField = field("password", String.class);

        final Field<UUID> idValues = context
                .select(idField)
                .from(extraUsersTable)
                .asField();

        final String newPassword = request.getNewPassword();
        final String originalPassword = request.getOriginalPassword();

        context.update(usersTable)
               .set(row(passwordField), row(inline(newPassword)))
               .where(idField.equal(inline(userId))
                             .and(passwordField.equal(inline(originalPassword)))
                             .and(inline(userId).in(idValues)))
               .execute();
    }

    private String extraUsersTableForRole(final UserRole role) {
        switch (role) {
            case CLIENT:
                return "clients";
            case ADMIN:
                return "admins";
            default:
                return null;
        }
    }
}
