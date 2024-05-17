package api.database;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;

@Configuration
@ComponentScan("api.database")
@Profile("test")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "api.database.repos",
entityManagerFactoryRef = "contestEntityManagerFactory",
transactionManagerRef = "contestTransactionManager")
@EnableConfigurationProperties
public class ContestDatabaseAutoConfiguration {
    @Bean
    @ConfigurationProperties("datasource.contest")
    public DataSourceProperties contestDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public JdbcTemplate contestJdbcTemplate() { return new JdbcTemplate(contestDataSource());}

    @Bean
    @ConfigurationProperties("datasource.contest.configuration")
    public HikariDataSource contestDataSource() {
        return contestDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean contestEntityManagerFactory(@Qualifier("contestBuilder") EntityManagerFactoryBuilder builder, DataSourceProperties dsp) {
        return builder.dataSource(contestDataSource()).packages("api.database.entities").build();
    }

    @Bean
    public PlatformTransactionManager contestTransactionManager(@Qualifier("contestEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean(name = "contestBuilder")
    public EntityManagerFactoryBuilder contestManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

}
