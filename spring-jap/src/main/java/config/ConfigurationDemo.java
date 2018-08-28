package config;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.hibernate.dialect.SQLServer2012Dialect;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"repositories"})
public class ConfigurationDemo {


    @Bean
    @Primary
    public DataSource getDataSource() {

        SQLServerDataSource ds = null;
        try {
            ds = new SQLServerDataSource();
//            ds.setInstanceName("sqlexpress");
            ds.setServerName("localhost");
            ds.setDatabaseName("idatest");
            ds.setIntegratedSecurity(true);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return ds;
    }


    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(Database.SQL_SERVER);
        return hibernateJpaVendorAdapter;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean abstractEntityManagerFactoryBean(
            JpaVendorAdapter jpaVendorAdapter) {

        Properties properties = new Properties();
        // The following two sections are needed to see hibernate values in the log
        properties.setProperty(FORMAT_SQL, String.valueOf(true));
        properties.setProperty(SHOW_SQL, String.valueOf(true));
//        properties.setProperty(USE_SQL_COMMENTS, String.valueOf(true));
        properties.setProperty(DIALECT, SQLServer2012Dialect.class.getTypeName());
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
                new LocalContainerEntityManagerFactoryBean();

        localContainerEntityManagerFactoryBean.setDataSource(getDataSource());
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        localContainerEntityManagerFactoryBean.setJpaProperties(properties);
        localContainerEntityManagerFactoryBean.setPackagesToScan("enteties");

        return localContainerEntityManagerFactoryBean;
    }


    @Bean
    public PlatformTransactionManager platformTransactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }


}
