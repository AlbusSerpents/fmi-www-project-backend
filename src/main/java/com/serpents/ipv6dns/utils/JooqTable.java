package com.serpents.ipv6dns.utils;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.util.HashMap;
import java.util.Map;

import static com.serpents.ipv6dns.utils.JooqField.*;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public enum JooqTable {

    DOMAIN_DETAILS(table("domain_details")) {
        @Override
        protected Map<JooqField<?>, Name> loadFieldsMap() {
            final Map<JooqField<?>, Name> fieldsMap = new HashMap<>();

            fieldsMap.put(ID, DSL.name("id"));
            fieldsMap.put(DOMAIN_NAME, DSL.name("domain_name"));
            fieldsMap.put(DESCRIPTION, DSL.name("description"));

            return fieldsMap;
        }
    },
    DOMAIN_REQUESTS(table("domain_requests")) {
        @Override
        protected Map<JooqField<?>, Name> loadFieldsMap() {
            final Map<JooqField<?>, Name> fieldsMap = new HashMap<>();

            fieldsMap.put(ID, DSL.name("id"));
            fieldsMap.put(CLIENT_ID, DSL.name("client_id"));
            fieldsMap.put(UPDATED_AT, DSL.name("updated_at"));
            fieldsMap.put(STATUS, DSL.name("status"));
            fieldsMap.put(DETAILS_ID, DSL.name("domain_details_id"));

            return fieldsMap;
        }
    },
    ADDRESSES(table("addresses")) {
        @Override
        protected Map<JooqField<?>, Name> loadFieldsMap() {
            final Map<JooqField<?>, Name> fieldsMap = new HashMap<>();

            fieldsMap.put(ID, DSL.name("id"));
            fieldsMap.put(ADDRESS, DSL.name("address"));
            return fieldsMap;
        }
    },
    FREE_ADDRESSES(table("free_addresses")) {
        @Override
        protected Map<JooqField<?>, Name> loadFieldsMap() {
            final Map<JooqField<?>, Name> fieldsMap = new HashMap<>();

            fieldsMap.put(ID, DSL.name("id"));
            fieldsMap.put(ADDRESS, DSL.name("address"));
            return fieldsMap;
        }
    },
    DOMAINS(table("domains")) {
        @Override
        protected Map<JooqField<?>, Name> loadFieldsMap() {
            final Map<JooqField<?>, Name> fieldsMap = new HashMap<>();

            fieldsMap.put(ID, DSL.name("id"));
            fieldsMap.put(OWNER, DSL.name("owner"));
            fieldsMap.put(DETAILS_ID, DSL.name("domain_details_id"));
            return fieldsMap;
        }
    },
    CLIENTS(table("clients")) {
        @Override
        protected Map<JooqField<?>, Name> loadFieldsMap() {
            final Map<JooqField<?>, Name> fieldsMap = new HashMap<>();

            fieldsMap.put(ID, DSL.name("id"));
            fieldsMap.put(EMAIL, DSL.name("email"));
            fieldsMap.put(FACULTY_NUMBER, DSL.name("faculty_number"));
            return fieldsMap;
        }
    },
    USERS(table("users")) {
        @Override
        protected Map<JooqField<?>, Name> loadFieldsMap() {
            final Map<JooqField<?>, Name> fieldsMap = new HashMap<>();

            fieldsMap.put(ID, DSL.name("id"));
            fieldsMap.put(USERNAME, DSL.name("username"));
            fieldsMap.put(PASSWORD, DSL.name("password"));
            fieldsMap.put(NAME, DSL.name("name"));
            return fieldsMap;
        }
    },
    CLIENT_USERS(table("client_users")) {
        @Override
        protected Map<JooqField<?>, Name> loadFieldsMap() {
            final Map<JooqField<?>, Name> fieldsMap = new HashMap<>();

            fieldsMap.put(ID, DSL.name("id"));
            fieldsMap.put(USERNAME, DSL.name("username"));
            fieldsMap.put(PASSWORD, DSL.name("password"));
            fieldsMap.put(NAME, DSL.name("name"));
            fieldsMap.put(EMAIL, DSL.name("email"));
            fieldsMap.put(FACULTY_NUMBER, DSL.name("faculty_number"));
            return fieldsMap;
        }
    },
    ADMIN_USERS(table("admin_users")) {
        @Override
        protected Map<JooqField<?>, Name> loadFieldsMap() {
            final Map<JooqField<?>, Name> fieldsMap = new HashMap<>();

            fieldsMap.put(ID, DSL.name("id"));
            fieldsMap.put(USERNAME, DSL.name("username"));
            fieldsMap.put(PASSWORD, DSL.name("password"));
            fieldsMap.put(NAME, DSL.name("name"));
            fieldsMap.put(EMAIL, DSL.name("email"));
            return fieldsMap;
        }
    };

    JooqTable(final Table<?> table) {
        this.jooqTable = table;
        fields = loadFieldsMap();
    }

    private final Table<?> jooqTable;
    private final Map<JooqField<?>, Name> fields;

    protected abstract Map<JooqField<?>, Name> loadFieldsMap();

    public Table<?> getTable() {
        return jooqTable;
    }


    public <T> Field<T> getField(final JooqField<T> field) {
        final Name fieldName = fields.get(field);
        final Name fullName = DSL.name(getTable().getName(), fieldName.first());
        return field(fullName, field.getFieldClass());
    }

}
