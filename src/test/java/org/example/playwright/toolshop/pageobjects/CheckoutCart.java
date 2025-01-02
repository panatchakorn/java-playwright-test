package org.example.playwright.toolshop.pageobjects;

import com.microsoft.playwright.Page;
import org.example.playwright.domain.CartLineItem;

import java.util.List;

public class CheckoutCart {
    private final Page page;

    public CheckoutCart(Page page) {
        this.page = page;
    }

    public List<CartLineItem> getLineItems() {
        page.locator("app-cart tbody tr").first().waitFor();
        return page.locator("app-cart tbody tr")
                .all()
                .stream()
                .map(row -> {
                            String title = trimmed(row.getByTestId("product-title")
                                    .innerText());
                            int quantity = Integer.parseInt(row.getByTestId("product-quantity")
                                    .inputValue());
                            double price = price(row.getByTestId("product-price")
                                    .innerText());
                            double lineTotal = price(row.getByTestId("line-price")
                                    .innerText());
                            return new CartLineItem(title, quantity, price, lineTotal);
                        }
                ).toList();

    }
    private String trimmed(String value) {
        return value.strip().replaceAll("\u00A0", "");
    }

       private Double price(String value) {
        return Double.parseDouble(value.replace("$", ""));
    }
}
