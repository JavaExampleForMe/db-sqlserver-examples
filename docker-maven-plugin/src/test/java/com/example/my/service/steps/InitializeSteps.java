package com.example.my.service.steps;

import com.example.my.service.ScriptConfig;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

@ContextConfiguration(classes = {ScriptConfig.class})
public class InitializeSteps {

    @Autowired
    public ScriptRunner scriptRunner;
    private Boolean isScriptRunSuccessfully;

    @When("^The following scripts will be executed (.*)$")
    public void theFollowingScriptsWillBeExecuted(String scriptName) throws FileNotFoundException {
        isScriptRunSuccessfully = true;
        Reader reader = new BufferedReader(new FileReader("./src/test/resources/dbscripts/" + scriptName + ".sql"));
        try {
            scriptRunner.runScript(reader);
        } catch (Exception e) {
            isScriptRunSuccessfully = false;
        }
    }

    @Then("^Script will finish successfully$")
    public void scriptWillFinishSuccessfully() {
        Assert.assertTrue(isScriptRunSuccessfully);
    }

}
