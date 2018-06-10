package com.serpents.ipv6dns.utils;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Param;

import static org.jooq.impl.DSL.inline;

public class JooqUtils {
    private JooqUtils() {

    }

    public static <T> Condition optionalFieldCondition(final Field<T> field, final T value) {
        final Param<T> param = inline(value);
        return inline(value).isNull().or(field.equal(param));
    }
}
