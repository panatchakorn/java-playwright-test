package org.example.playwright.toolshop.cucumber.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ProductCatalogStepDefinitions {
    @Given("Sally is on the homepage")
    public void sallyIsOnTheHomepage() {
        System.out.println("Sally is on the homepage");
    }

    @When("she searches for {string}")
    public void sheSearchesFor(String productName) {
    }
    @Then("the {string} product should be displayed")
    public void theProductShouldBeDisplayed(String productName) {
    }
}
