package org.example.playwright.toolshop.catalogue;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.example.playwright.toolshop.fixtures.PlaywrightTestCase;
import org.example.playwright.toolshop.pageobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SearchProductsTest extends PlaywrightTestCase {

    SearchComponent searchComponent;
    ProductList productList;
    NavigationBar navigationBar;

    @Step("Setup go to homepage")
    @BeforeEach
    public void setup() {
        searchComponent = new SearchComponent(page);
        productList = new ProductList(page);
        navigationBar = new NavigationBar(page);
        navigationBar.openHomePage();
    }

    @DisplayName("Search products By Keyword")
    @Test
    void searchProducts() {

        searchComponent.searchBy("tape");
        var matchingProducts = productList.getProductNames();
        Allure.step("Verify matching products: " + matchingProducts);
        Assertions.assertThat(matchingProducts)
                .contains("Tape Measure 7.5m", "Measuring Tape", "Tape Measure 5m");
    }

    @DisplayName("Search products that do not exist")
    @Test
    void searchProductsThatDoNotExist() {
        searchComponent.searchBy("unknown");
        var matchingProducts = productList.getProductNames();

        Allure.step("Verify no products found");
        Assertions.assertThat(matchingProducts)
                .isEmpty();
        Assertions.assertThat(productList.getSearchCompletedMessage()).contains("There are no products found.");
    }

    @DisplayName("Clear search products")
    @Test
    void clearSearch() {
        searchComponent.searchBy("saw");
        var matchingProducts = productList.getProductNames();

        Assertions.assertThat(matchingProducts).hasSize(2);
        searchComponent.clearSearch();

        var defaultProducts = productList.getProductNames();
        Assertions.assertThat(defaultProducts).hasSize(9);
    }
}
