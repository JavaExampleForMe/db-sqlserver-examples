package com.example.controller.stream;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@SpringBootApplication
public class DemoStreamApplication {


    @Autowired
    DataSource dataSource;


    @Bean
    public DataSource getDataSource() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName("localhost");
        ds.setDatabaseName("idatest");
        ds.setIntegratedSecurity(true);
        return ds;
    }


    public static void main(String[] args) {
        SpringApplication.run(DemoStreamApplication.class, args);
    }
}
