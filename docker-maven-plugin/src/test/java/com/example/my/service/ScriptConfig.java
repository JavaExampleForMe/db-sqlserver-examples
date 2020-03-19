package com.example.my.service;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@PropertySource("classpath:application.yml")
public class ScriptConfig {

    @Value("${database.server}")
    String dbServer;
    @Value("${database.port}")
    String dbPort;
    @Value("${database.user}")
    String dbUsername;
    @Value("${database.password}")
    String dbPassword;

    @Bean
    public ScriptRunner getScriptRunner() throws SQLException {
        String connectionUrl = String.format("jdbc:sqlserver://%s:%s;databaseName=master;user=%s;password=%s;integratedSecurity=false",
                dbServer,
                dbPort,
                dbUsername,
                dbPassword);
        Connection connection = DriverManager.getConnection(connectionUrl);
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setDelimiter("GO");
        scriptRunner.setStopOnError(true);
        return scriptRunner;
    }

}
