package org.example.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.LoadState;
import org.assertj.core.api.Assertions;
import org.example.playwright.browser.ChromiumBrowser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

@UsePlaywright(ChromiumBrowser.class)
public class AssertionTest {

    @DisplayName("Making assertions about data values")
    @Nested
    class DataAssertions {

        @BeforeEach
        void productsHaveCorerctValue(Page page, Playwright playwright) {
            page.navigate("https://practicesoftwaretesting.com");
            playwright.selectors().setTestIdAttribute("data-test");
            page.waitForCondition(() -> page.getByTestId("product-name").count() > 0);

        }

        @Test
        void allProductsPricesShouldHaveCorrectValues(Page page) {
            List<Double> prices = page.getByTestId("product-price")
                    .allInnerTexts()
                    .stream()
                    .map(price -> Double.parseDouble(price.replace("$", "")))
                    .toList();
            Assertions.assertThat(prices)
                    .isNotEmpty()
                    .allMatch(price -> price > 5.00)
                    .allSatisfy(price -> Assertions.assertThat(price)
                            .isGreaterThan(5.00)
                            .isLessThan(100.00));
        }

        @Test
        void shouldSortInAlphabeticalOrder(Page page) {
            page.getByLabel("Sort").selectOption("Name (A - Z)");
            page.waitForLoadState(LoadState.NETWORKIDLE);

            List<String> productNames = page.getByTestId("product-name")
                    .allInnerTexts();
            Assertions.assertThat(productNames).isSortedAccordingTo(String.CASE_INSENSITIVE_ORDER);
        }

        @Test
        void shouldSortInReverseAlphabeticalOrder(Page page) {
            page.getByLabel("Sort").selectOption("Name (Z - A)");
            page.waitForLoadState(LoadState.NETWORKIDLE);

            List<String> productNames = page.getByTestId("product-name")
                    .allInnerTexts();
            Assertions.assertThat(productNames).isSortedAccordingTo(Comparator.reverseOrder());
        }
    }
}
