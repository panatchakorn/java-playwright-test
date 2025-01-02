package org.example.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class LocatorsTest {
    Playwright playwright;
    Browser browser;
    Page page;


    @DisplayName("Locating Elements by Text")
    @Nested
    class LocatingElementsByText {
        @BeforeEach
        void setUp() {

            playwright = Playwright.create();
            browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setArgs(Arrays.asList("--no-sandbox", "--start-maximized", "--incognito", "--disable-extensions", "--disable-gpu"))
                    );
            page = browser.newPage();
            page.navigate("https://practicesoftwaretesting.com/");
        }

        @AfterEach
        void tearDown() {
            browser.close();
            playwright.close();
        }

        @DisplayName("Locating an element by text contents")
        @Test
        void byText() {
            page.getByText("Bolt Cutters")
                    .click();
            PlaywrightAssertions.assertThat(page.getByText("MightyCraft Hardware "))
                    .isVisible();
        }

        @DisplayName("Locating an element by alt text contents")
        @Test
        void byAltText() {
            page.getByAltText("Combination Pliers")
                    .click();
            PlaywrightAssertions.assertThat(page.getByText("ForgeFlex Tools"))
                    .isVisible();
        }

        @DisplayName("Locating an element by aria role")
        @Test
        void byAriaRole() {
            page.getByPlaceholder("Search")
                    .fill("pliers");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"))
                    .click();
            PlaywrightAssertions.assertThat(page.getByText("Searched for: pliers"))
                    .isVisible();
        }

        @DisplayName("Locating an element by test IDs")
        @Test
        void byTestId() {
            playwright.selectors()
                    .setTestIdAttribute("data-test");
            page.getByTestId("search-query")
                    .fill("pliers");
            page.getByTestId("search-submit")
                    .click();
            PlaywrightAssertions.assertThat(page.getByText("Searched for: pliers"))
                    .isVisible();
        }

        @DisplayName("Locating an element by card")
        @Test
        void byCard() {
            int itemsOnThePage = page.locator(".card")
                    .count();
//            page.locator(".card").first().click();
            page.locator(".card")
                    .nth(itemsOnThePage - 1)
                    .click();
//            List<String> itemNames = page.getByTestId("product-name").allTextContents();
            PlaywrightAssertions.assertThat(page.getByText("ForgeFlex Tools"))
                    .isVisible();
        }

        @DisplayName("Locating an element by css")
        @Test
        void byCSS() {
            page.getByText("Contact")
                    .click();
            // by css id
            page.locator("#first_name")
                    .fill("Sarah-Jane");
            PlaywrightAssertions.assertThat(page.locator("#first_name"))
                    .hasValue("Sarah-Jane");
            // by css class
            page.locator(".btnSubmit")
                    .click();
            List<String> alertMessages = page.locator(".alert")
                    .allTextContents();
            for (String message : alertMessages) {
                Assertions.assertTrue(message.contains("is required"));
            }
            // by css attribute
            page.locator("input[placeholder='Your last name *']")
                    .fill("Smith");
            PlaywrightAssertions.assertThat(page.locator("#last_name"))
                    .hasValue("Smith");
        }

        @DisplayName("Locating elements in nested elements")
        @Test
        void byNestedElements() {
            page.getByRole(AriaRole.MENUBAR, new Page.GetByRoleOptions().setName("Main Menu"))
                    .getByText("Contact")
                    .click();
            page.locator("#first_name")
                    .fill("Sarah-Jane");
            PlaywrightAssertions.assertThat(page.locator("#first_name"))
                    .hasValue("Sarah-Jane");
        }

        @DisplayName("Filter elements")
        @Test
        void filterElements() {
            page.locator("label:has-text('Power Tools') input[type='checkbox']").click();
            page.waitForLoadState(LoadState.NETWORKIDLE);
            playwright.selectors().setTestIdAttribute("data-test");
            List<String> allProducts = page.getByTestId("product-name")
                    .filter(new Locator.FilterOptions().setHasText("Sander"))
                    .allTextContents();
            Assertions.assertEquals(2,allProducts.size());

            page.getByRole(AriaRole.MENUBAR, new Page.GetByRoleOptions().setName("Main Menu"))
                    .getByText("Home")
                    .click();
            page.locator("label:has-text('Drill') input[type='checkbox']").click();
            page.waitForLoadState(LoadState.NETWORKIDLE);
            List<String> productNames = page.getByTestId("product-name")
                    .allTextContents();
            assertThat(productNames).allMatch(name -> name.contains("Drill"));

            page.getByRole(AriaRole.MENUBAR, new Page.GetByRoleOptions().setName("Main Menu"))
                    .getByText("Home")
                    .click();
            page.waitForLoadState(LoadState.NETWORKIDLE);
            Locator outOfStockItem = page.locator(".card")
                    .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                    .getByTestId("product-name");
            PlaywrightAssertions.assertThat(outOfStockItem).hasText("Long Nose Pliers");
        }
    }

}
