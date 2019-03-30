package com.github.tylerhaigh.tourofheros.cucumber.steps;

import com.github.tylerhaigh.tourofheros.cucumber.CucumberTest;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class HerosSteps {

    @Before
    public void beforeScenario() {
        System.out.println("=== START OF SCENARIO");
        CucumberTest.muleServer().setUpMunit();
    }

    @After
    public void afterScenario() throws Throwable {
        System.out.println("=== END OF SCENARIO");
        CucumberTest.muleServer().restartMunit();
    }

    @When("a hero is posted to the api")
    public void a_hero_is_posted_to_the_api() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the hero is saved in the database")
    public void the_hero_is_saved_in_the_database() {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }
}
