package org.example.playwright.toolshop.pageobjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class SearchComponent {
    private final Page page;

    public SearchComponent(Page page) {
        this.page = page;
    }

    @Step("Search by keyword: {keyword}")
    public void searchBy(String keyword) {
        https://api.practicesoftwaretesting.com/products/search?q=Adjustable%20Wrench
        page.waitForResponse("**/products/search?q=**", () -> {
            page.getByPlaceholder("Search")
                    .fill(keyword);
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"))
                    .click();
        });
    }

    @Step("Clear search")
    public void clearSearch() {
        page.waitForResponse("**/products**", () -> {
            page.getByTestId("search-reset").click();
        });
    }

    public void filterBy(String filterName) {
        //https://api.practicesoftwaretesting.com/products?between=price,1,100&by_category=01JGHTPAH06H39VHG8K8K0HDE3&page=0
        page.waitForResponse("**/products?**by_category=**", () -> {
            page.getByLabel(filterName).click();
        });
    }
}
