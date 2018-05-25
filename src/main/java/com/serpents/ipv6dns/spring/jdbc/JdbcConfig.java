package com.serpents.ipv6dns.spring.jdbc;


import com.serpents.ipv6dns.spring.PropertiesConfig;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

import static com.serpents.ipv6dns.spring.PropertiesConfig.ApplicationProperty.JDBC_URL;

@SuppressWarnings("unused")
@Configuration
@EnableWebMvc
public class JdbcConfig {
    private final String databaseUrl;

    @Autowired
    public JdbcConfig(final PropertiesConfig propertiesConfig) {
        databaseUrl = propertiesConfig.getProperty(JDBC_URL);
    }

    @Bean(destroyMethod = "close")
    public DataSource getDataSource() {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(databaseUrl);
        return dataSource;
    }

    @Bean
    public NamedParameterJdbcOperations jdbcOperations(final DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager getTransactionManager(final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
