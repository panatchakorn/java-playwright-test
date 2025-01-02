package org.example.playwright.toolshop.cucumber.stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.example.playwright.domain.ProductSummary;
import org.example.playwright.toolshop.pageobjects.NavigationBar;
import org.example.playwright.toolshop.pageobjects.ProductList;
import org.example.playwright.toolshop.pageobjects.SearchComponent;

import java.util.List;
import java.util.Map;

public class ProductCatalogStepDefinitions {
    NavigationBar navigationBar;
    SearchComponent searchComponent;
    ProductList productList;


    @Before
    public void setUp() {
        navigationBar = new NavigationBar(PlaywrightCucumberFixtures.getPage());
        searchComponent = new SearchComponent(PlaywrightCucumberFixtures.getPage());
        productList = new ProductList(PlaywrightCucumberFixtures.getPage());
    }

    @Given("Sally is on the homepage")
    public void sallyIsOnTheHomepage() {
        System.out.println("Sally is on the homepage");
        navigationBar.openHomePage();
    }

    @When("she searches for {string}")
    public void sheSearchesFor(String searchTerm) {
        searchComponent.searchBy(searchTerm);
    }

    @Then("the {string} product should be displayed")
    public void theProductShouldBeDisplayed(String productName) {
        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts)
                .contains(productName);
    }

    @DataTableType
    public ProductSummary productSummaryRow(Map<String, String> productData) {
        return new ProductSummary(productData.get("Name"), productData.get("Price"));
    }

    @Then("the following products should be displayed")
    public void theFollowingProductsShouldBeDisplayed(List<ProductSummary> expectedProductSummaries) { //DataTable expectedProducts
        var matchingProducts = productList.getProductSummaries();
        /*List<Map<String,String>> expectedProductData = expectedProducts.asMaps();
        List<ProductSummary> expectedProductSummaries = expectedProductData.stream()
                .map(productData -> new ProductSummary(productData.get("Name"), productData.get("Price")))
                .toList();*/
        Assertions.assertThat(matchingProducts)
                .containsExactlyElementsOf(expectedProductSummaries);
    }

    @Then("no products should be displayed")
    public void noProductsShouldBeDisplayed() {
        List<ProductSummary> matchingProducts = productList.getProductSummaries();
        Assertions.assertThat(matchingProducts)
                .isEmpty();
    }

    @And("the message {string} should be displayed")
    public void theMessageShouldBeDisplayed(String messageText) {
        String completionMessage = productList.getSearchCompletedMessage();
        Assertions.assertThat(completionMessage)
                .isEqualTo(messageText);
    }

    @And("she filters by {string}")
    public void sheFiltersBy(String filterName) {
        searchComponent.filterBy(filterName);
    }

    @When("she sorts by {string}")
    public void sheSortsBy(String sortFilter) {
        productList.sortBy(sortFilter);
    }

    @Then("the first product displayed should be {string}")
    public void theFirstProductDisplayedShouldBe(String firstProductName) {
        List<String> productNames = productList.getProductNames();
        Assertions.assertThat(productNames).startsWith(firstProductName);
    }
}
