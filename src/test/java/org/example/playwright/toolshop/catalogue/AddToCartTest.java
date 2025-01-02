package org.example.playwright;

import org.assertj.core.api.Assertions;
import org.example.playwright.domain.CartLineItem;
import org.example.playwright.toolshop.fixtures.PlaywrightTestCase;
import org.example.playwright.toolshop.pageobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AddToCartTest extends PlaywrightTestCase {

    SearchComponent searchComponent;
    ProductList productList;
    ProductDetails productDetails;
    NavigationBar navigationBar;
    CheckoutCart checkoutCart;

    @BeforeEach
    public void setup() {
        searchComponent = new SearchComponent(page);
        productList = new ProductList(page);
        productDetails = new ProductDetails(page);
        navigationBar = new NavigationBar(page);
        checkoutCart = new CheckoutCart(page);
        navigationBar.openHomePage();
    }

    @DisplayName("View products after add to cart")
    @Test
    void viewProductsAfterAddToCart() {
        searchComponent.searchBy("pliers");
        productList.viewProductDetails("Combination Pliers");
        productDetails.increaseQuantityBy(2);
        productDetails.addToCart();
        navigationBar.openCart();
        List<CartLineItem> lineItems = checkoutCart.getLineItems();
        Assertions.assertThat(lineItems)
                .hasSize(1)
                .first()
                .satisfies(item -> {
                    Assertions.assertThat(item.title())
                            .contains("Combination Pliers");
                    Assertions.assertThat(item.quantity())
                            .isEqualTo(3);
                    Assertions.assertThat(item.total())
                            .isEqualTo(item.quantity() * item.price());
                });
    }

    @DisplayName("View products after add multiple items to cart")
    @Test
    void viewProductsAfterAddingMultipleItemsToCart() {
        productList.viewProductDetails("Bolt Cutters");
        productDetails.increaseQuantityBy(2);
        productDetails.addToCart();

        navigationBar.clickHome();
        productList.viewProductDetails("Slip Joint Pliers");
        productDetails.addToCart();

        navigationBar.openCart();

        List<CartLineItem> lineItems = checkoutCart.getLineItems();

        Assertions.assertThat(lineItems).hasSize(2);
        List<String> productNames = lineItems.stream()
                .map(CartLineItem::title)
                .toList();

        Assertions.assertThat(productNames).contains("Bolt Cutters", "Slip Joint Pliers");
        Assertions.assertThat(lineItems)
                .allSatisfy(item -> {
                    Assertions.assertThat(item.quantity()).isGreaterThanOrEqualTo(1);
                    Assertions.assertThat(item.price()).isGreaterThan(0.0);
                    Assertions.assertThat(item.total()).isEqualTo(item.quantity() * item.price());
                });
    }
}
