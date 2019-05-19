import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import enteties.DatesTbl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;

@EnableTransactionManagement
public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        Dml dml = new Dml();
        dml.getDatesTbl();

        EntityManagerFactory entityManagerFactory = context.getBean(EntityManagerFactory.class);
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final LocalDateTime creationDate = LocalDateTime.of(2018, 12, 26, 8, 10, 40, 89100000);
        entityManager.getTransaction().begin();
        final DatesTbl datesTbl = entityManager.merge(new DatesTbl(roundNanoSecForDateTime(creationDate)));
        entityManager.getTransaction().commit();

//        entityManager.persist(new DatesTbl(creationDate));

        System.out.println("test");
    }

    private static LocalDateTime roundNanoSecForDateTime(LocalDateTime localDateTime) {
        int nanoSec = localDateTime.getNano();
        // The rounding is based on following results on SQL server 2012 express
        // select cast(cast('2018-12-26 08:10:40.3414999' as datetime2) as datetime);
        // 2018-12-26 08:10:40.340
        // select cast(cast('2018-12-26 08:10:40.3415000' as datetime2) as datetime);
        // select cast(cast('2018-12-26 08:10:40.3444999' as datetime2) as datetime);
        // 2018-12-26 08:10:40.343
        // select cast(cast('2018-12-26 08:10:40.3445000' as datetime2) as datetime);
        // select cast(cast('2018-12-26 08:10:40.3484999' as datetime2) as datetime);
        // 2018-12-26 08:10:40.347
        // select cast(cast('2018-12-26 08:10:40.3485000' as datetime2) as datetime);
        // 2018-12-26 08:10:40.350
        int last7DigitOfNano = nanoSec - (nanoSec / 10000000) * 10000000;
        int roundedNanoSec = 0;
        if (last7DigitOfNano < 1500000) {
            roundedNanoSec = nanoSec - last7DigitOfNano;
        } else if (last7DigitOfNano < 4500000) {
            roundedNanoSec = nanoSec - last7DigitOfNano + 3000000;
        } else if (last7DigitOfNano < 8500000) {
            roundedNanoSec = nanoSec - last7DigitOfNano + 7000000;
        } else {
            roundedNanoSec = nanoSec - last7DigitOfNano + 10000000;
        }
        System.out.println("Before Rounding" + nanoSec);
        System.out.println("After Rounding" + roundedNanoSec);
        return localDateTime.withNano(roundedNanoSec);
    }

    @Bean
    public DataSource getDataSource() {

        SQLServerDataSource ds = null;
        try {
            ds = new SQLServerDataSource();
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
         properties.setProperty(FORMAT_SQL, String.valueOf(true));
        properties.setProperty(SHOW_SQL, String.valueOf(true));
        properties.setProperty(DIALECT, ModifiedSQLServerDialect.class.getTypeName());
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

    public static JdbcTemplate getJdbcTemplate()  {
        SQLServerDataSource ds = new SQLServerDataSource();
        //ds.setInstanceName("RED14\SQL2012_DEV");
        ds.setServerName("localhost");
        ds.setDatabaseName("idatest");
        ds.setIntegratedSecurity(true);
        return new JdbcTemplate(ds);
    }

}
