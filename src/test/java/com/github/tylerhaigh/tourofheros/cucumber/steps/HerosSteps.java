package com.github.tylerhaigh.tourofheros.cucumber.steps;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import com.github.tylerhaigh.tourofheros.cucumber.CucumberTest;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class HerosSteps {

    MuleEvent outputEvent = null;

    @Before
    public void beforeScenario() {
        System.out.println("=== START OF SCENARIO");
        CucumberTest.muleServer().setUpMunit();
        this.outputEvent = null;
    }

    @After
    public void afterScenario() throws Throwable {
        System.out.println("=== END OF SCENARIO");
        CucumberTest.muleServer().restartMunit();
    }

    @When("a hero is posted to the api")
    public void a_hero_is_posted_to_the_api() throws Exception {

        // Setup Mocks
        MuleMessage mockMessage = CucumberTest.muleServer().mockMuleMessageWithPayload("{ \"id\": 1, \"name\": \"Superman\" }");

        CucumberTest.muleServer()
            .mockWhenMessageProcessor("invoke")
            .withAttributes(
                new HashMap<String, Object>() {{
                    put("method", "insertDocument");
                }}
            )
            .thenReturn(
                CucumberTest.muleServer().mockMuleMessageWithPayload(mockMessage)
            );

        MuleEvent evnt = CucumberTest.muleServer().mockTestEvent("{ \"name\": \"Superman\" }");
        this.outputEvent = CucumberTest.muleServer().runMuleFlow("heros-api:/post-hero", evnt);
    }

    @Then("the hero is saved in the database")
    public void the_hero_is_saved_in_the_database() throws Exception {
        int httpStatus = (Integer)outputEvent.getMessage().getOutboundProperty("http.status");
        assertEquals(201, httpStatus);

        String expectedPayload = "{ \"id\": 1, \"name\": \"Superman\" }";
        String actualPayload = outputEvent.getMessage().getPayloadAsString();
        assertEquals(expectedPayload, actualPayload);
    }
}
