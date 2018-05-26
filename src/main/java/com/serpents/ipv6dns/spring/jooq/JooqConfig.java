package com.serpents.ipv6dns.spring.jooq;


import com.serpents.ipv6dns.spring.PropertiesConfig;
import org.apache.commons.dbcp2.BasicDataSource;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.serpents.ipv6dns.spring.PropertiesConfig.ApplicationProperty.JDBC_URL;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.DSL.using;

@SuppressWarnings("unused")
@Configuration
@EnableWebMvc
public class JooqConfig {
    private final String databaseUrl;

    @Autowired
    public JooqConfig(final PropertiesConfig propertiesConfig) {
        databaseUrl = propertiesConfig.getProperty(JDBC_URL);
    }

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(databaseUrl);
        return dataSource;
    }

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
