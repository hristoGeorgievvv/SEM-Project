package nl.tudelft.sem.sem54.mainservice.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * This wont run on CI.
 * By default spring should run h2 if there's no other entity manager defined.
 */
@Configuration
@Profile("!ci&!dev")
@PropertySource("classpath:application.properties")
public class PersistanceConfig {

    @Value("${db.password}")
    String databasePassword;

    @Value("${db.username}")
    String databseUsername;

    @Value("${db.url}")
    String databaseUrl;

    /**
     * Creates entity manager.
     *
     * @return Mariadb entitymanager
     */
    @Bean("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        // Important as this is where we tell hibernate where to look for our entities.
        em.setPackagesToScan("nl.tudelft.sem.sem54.mainservice.entities");
        em.setJpaVendorAdapter(vendorAdapter);
        em.setDataSource(dataSource());
        em.setJpaProperties(additionalProperties());
        return em;
    }

    /**
     * Creates datasource.
     *
     * @return Datasource bean initialization.
     */
    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl(databaseUrl);

        dataSource.setUsername(databseUsername);
        dataSource.setPassword(databasePassword);

        return dataSource;
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        // Available options for this are 'create', 'create-drop', 'update' and 'verify'
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MariaDB103Dialect");

        return properties;
    }
}
