package org.example.playwright.toolshop.pageobjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class ProductDetails {
    private final Page page;

    public ProductDetails(Page page) {
        this.page = page;
    }

    public void increaseQuantityBy(int quantity) {
        for (int i = 1; i <= quantity; i++)
            page.getByTestId("increase-quantity")
                    .click();
    }

    @Step("Add to cart")
    public void addToCart() {

        page.waitForResponse(
                response -> response.url().contains("/carts") && response.request().method().equals("POST"),
                () -> {
                    page.getByText("Add to cart").click();
                    page.getByRole(AriaRole.ALERT).click();
                }
        );
        /*page.waitForCondition(() -> page.getByTestId("cart-quantity")
                .textContent()
                .equals("3"));*/
    }
}
