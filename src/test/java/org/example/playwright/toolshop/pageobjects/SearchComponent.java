package org.example.playwright.toolshop.search;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class SearchComponent {
    private final Page page;

    SearchComponent(Page page) {
        this.page = page;
    }

    public void searchBy(String keyword) {
        page.waitForResponse("**/search?query=" + keyword, () -> {
            page.getByPlaceholder("Search")
                    .fill(keyword);
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"))
                    .click();
        });
    }
}
