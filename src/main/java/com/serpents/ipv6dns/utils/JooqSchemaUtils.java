package com.serpents.ipv6dns.utils;

import com.serpents.ipv6dns.domain.request.DomainRequestStatus;
import org.jooq.Field;
import org.jooq.Table;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.serpents.ipv6dns.utils.JooqField.*;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public enum JooqSchemaUtils {

    DOMAIN_DETAILS(table("domain_details").as("dd")) {
        @Override
        protected Map<JooqField, Field<?>> loadFieldsMap() {
            final Map<JooqField, Field<?>> fieldsMap = new HashMap<>();

            fieldsMap.put(ID, field("dd.id", UUID.class));
            fieldsMap.put(DOMAIN_NAME, field("dd.domain_name", String.class));
            fieldsMap.put(DESCRIPTION, field("dd.description", String.class));

            return fieldsMap;
        }
    },

    DOMAIN_REQUESTS(table("domain_requests").as("dr")) {
        @Override
        protected Map<JooqField, Field<?>> loadFieldsMap() {
            final Map<JooqField, Field<?>> fieldsMap = new HashMap<>();

            fieldsMap.put(ID, field("dr.id", UUID.class));
            fieldsMap.put(CLIENT_ID, field("dr.client_id", UUID.class));
            fieldsMap.put(UPDATED_AT, field("dr.updated_at", LocalDateTime.class));
            fieldsMap.put(STATUS, field("dr.status", DomainRequestStatus.class));
            fieldsMap.put(DETAILS_ID, field("dr.domain_details_id", UUID.class));

            return fieldsMap;
        }
    };


    JooqSchemaUtils(final Table<?> table) {
        this.jooqTable = table;
        fields = loadFieldsMap();
    }

    private final Table<?> jooqTable;
    private final Map<JooqField, Field<?>> fields;

    protected abstract Map<JooqField, Field<?>> loadFieldsMap();

    public Table<?> getTable() {
        return jooqTable;
    }

    @SuppressWarnings("unchecked")
    public <T> Field<T> getField(final JooqField<T> field) {
        return (Field<T>) fields.get(field);
    }

}
