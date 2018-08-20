package com.serpents.ipv6dns.spring.config;


import com.serpents.ipv6dns.spring.properties.ApplicationProperties;
import org.apache.commons.dbcp2.BasicDataSource;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

import static com.serpents.ipv6dns.spring.properties.ApplicationPropertiesKey.JDBC_URL;
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.impl.DSL.using;

@SuppressWarnings("unused")
@Configuration
@EnableWebMvc
public class JooqConfig {
    private final String databaseUrl;

    @Autowired
    public JooqConfig(final ApplicationProperties applicationProperties) {
        databaseUrl = applicationProperties.getProperty(JDBC_URL);
    }

    @Bean
    public DataSource dataSource() {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(databaseUrl);
        return new TransactionAwareDataSourceProxy(dataSource);
    }

    @Bean
    @Primary
    public ConnectionProvider connectionProvider(final DataSource dataSource) {
        return new DataSourceConnectionProvider(dataSource);
    }

    @Bean
    public org.jooq.Configuration configuration(
            final DataSource dataSource,
            final ConnectionProvider provider,
            final DefaultExecuteListenerProvider executeListenerProvider) {
        return new DefaultConfiguration()
                .derive(POSTGRES)
                .derive(provider)
                .derive(executeListenerProvider);
    }


    @Bean
    public DSLContext dslContext(final org.jooq.Configuration configuration) {
        return using(configuration);
    }

    @Bean(name = "transactionManager")
    @Autowired
    public PlatformTransactionManager getTransactionManager(final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
