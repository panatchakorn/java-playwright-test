package org.example.playwright.toolshop.pageobjects;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class NavigationBar {
    private final Page page;

    public NavigationBar(Page page) {
        this.page = page;
    }

    @Step("Open cart")
    public void openCart() {
        page.getByTestId("nav-cart")
                .click();
    }

    @Step("Open home page")
    public void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com/");
    }

    @Step("Click home")
    public void clickHome() {
        page.getByTestId("nav-home")
                .click();
    }
}
