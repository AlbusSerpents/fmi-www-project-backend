package com.serpents.ipv6dns.spring.jdbc;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.DSL.using;

@Configuration
public class JooqConfiguration {

    @Bean
    public DSLContext dslContext(final DataSource dataSource) {
        try {
            return using(dataSource.getConnection(), POSTGRES);
        } catch (SQLException sqle) {
            System.err.println("Couldn't initialize connection");
            throw new RuntimeException(sqle);
        }
    }

}
