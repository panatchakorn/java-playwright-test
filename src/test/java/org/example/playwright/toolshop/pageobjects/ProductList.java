package org.example.playwright.toolshop.pageobjects;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.example.playwright.domain.ProductSummary;

import java.util.List;

public class ProductList {
    private final Page page;

    public ProductList(Page page) {
        this.page = page;
    }

    public List<String> getProductNames() {
        return page.getByTestId("product-name")
                .allInnerTexts();
    }

    public List<ProductSummary> getProductSummaries() {
        return page.locator(".card")
                .all()
                .stream()
                .map(productCard -> {
                    String productName = productCard.getByTestId("product-name")
                            .textContent().strip();
                    String productPrice = productCard.getByTestId("product-price")
                            .textContent();
                    return new ProductSummary(productName, productPrice);
                })
                .toList();
    }

    @Step("View product details: {productName}")
    public void viewProductDetails(String productName) {
        page.locator(".card").getByText(productName).click();
    }

    public String getSearchCompletedMessage() {
        return page.getByTestId("search_completed")
                .textContent();
    }

    public void sortBy(String sortFilter) {
        // https://api.practicesoftwaretesting.com/products?sort=name,asc&between=price,1,100&page=0
        page.waitForResponse("**/products?sort=**", () -> {
            page.getByTestId("sort")
                    .selectOption(sortFilter);
        });
    }
}
