package com.example.my.service;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import net.jcip.annotations.NotThreadSafe;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features/cleanup",
        glue = {"com.example.my.service.steps"},
        strict = true)
@ContextConfiguration(classes = {ScriptConfig.class})
@Configuration
@NotThreadSafe
public class InitializeRunnerTest {

}
