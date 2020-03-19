package com.example.my.service;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import net.jcip.annotations.NotThreadSafe;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features/cleanup",
        glue = {"com.example.my.service.steps"},
        strict = true)
@ContextConfiguration(classes = {ScriptConfig.class})
@Configuration
@NotThreadSafe
public class InitializeRunnerTest {
    @ClassRule
    public static final DockerComposeContainer container =
            new DockerComposeContainer(new File("./src/test/resources/docker-compose.yml"))
                    .withExposedService("sqlserver", 1433);
}
