package com.example.search.impl;

import com.example.search.api.DBDetails;
import com.example.search.impl.model2.SearchTaskEntity;
import lombok.val;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.dialect.SQLServer2012Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
@EnableJpaRepositories(
        basePackages = "com.example.search.impl.model2",
        entityManagerFactoryRef = "searchEntityManagerFactory2",
        transactionManagerRef = "searchTransactionManager2"
)
public class SearchConfigInternal2 {


    @Autowired
    private Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean searchEntityManagerFactory2() throws Exception{
        val bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(searchSourceData2());
        bean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        bean.setJpaPropertyMap(jpaProperties());
        bean.setPackagesToScan(SearchTaskEntity.class.getPackage().getName());
        bean.setPersistenceUnitName("search2");
        //bean.setPackagesToScan("com.example.search.impl.model2");
        return bean;
    }


    @Bean
    public DataSource searchSourceData2() throws Exception{
        BasicDataSource dataSource = new BasicDataSource();
        DBDetails dbDetails = getDbDetailsFromProperties();
        dataSource.setDriverClassName(com.microsoft.sqlserver.jdbc.SQLServerDriver.class.getName());
        String url = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;integratedSecurity=false",
                dbDetails.getHost(),
                dbDetails.getPort(),
                dbDetails.getDatabase());
        dataSource.setUrl(url);
        dataSource.setUsername(dbDetails.getUsername());// for sqlserver authentication
        dataSource.setPassword(dbDetails.getPassword());// for sqlserver authentication
        return dataSource;
    }

    private DBDetails getDbDetailsFromProperties() throws Exception {
        DBDetails dbDetails = new DBDetails();
        dbDetails.setDatabase("idatest1");
        dbDetails.setSchema("dbo");
        dbDetails.setUsername("sa");
        dbDetails.setPassword("sa");
        dbDetails.setHost("localhost");
        dbDetails.setPort("1433");
        if (!dbDetails.verifyAllFieldsAreFull()) {
            throw new Exception("No Databases were configured");
        }
        return dbDetails;
    }


    protected Map<String, Object> jpaProperties() {
        val properties = new HashMap<String, Object>();

        properties.put(DEFAULT_SCHEMA, "dbo");
        properties.put(STATEMENT_BATCH_SIZE, "50");
        properties.put(USE_NEW_ID_GENERATOR_MAPPINGS, false);
        properties.put(DIALECT, SQLServer2012Dialect.class.getTypeName());

        //      if (debugDB) {
        properties.put(SHOW_SQL, Boolean.toString(true));
        properties.put(FORMAT_SQL, Boolean.toString(true));
        //    }

        return properties;
    }

    @Bean
    public PlatformTransactionManager searchTransactionManager2() throws Exception {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                searchEntityManagerFactory2().getObject());
        return transactionManager;
    }
}
