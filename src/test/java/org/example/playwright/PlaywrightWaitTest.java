package org.example.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.assertj.core.api.Assertions;
import org.example.playwright.browser.ChromiumBrowser;
import org.junit.jupiter.api.*;

import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(ChromiumBrowser.class)
public class PlaywrightWaitTest {
    @DisplayName("Using PlayWright WaitForState")
    @Nested
    class WaitForState {
        @BeforeEach
        void setUp(Page page, Playwright playwright) {
            page.navigate("https://practicesoftwaretesting.com/");
            playwright.selectors()
                    .setTestIdAttribute("data-test");
//            page.waitForSelector("[data-test=product-name]");

            page.waitForSelector(".card-img-top");
        }

        @Test
        void shouldShowAllProductNames(Page page) {
            List<String> productNames = page.getByTestId("product-name")
                    .allInnerTexts();
            Assertions.assertThat(productNames)
                    .contains("Bolt Cutters", "Pliers", "Hammer");
        }

        @Test
        void shouldShowAllProductImages(Page page) {
            List<String> productImages = page.locator(".card-img-top")
                    .all()
                    .stream()
                    .map(img -> img.getAttribute("alt"))
                    .toList();
            Assertions.assertThat(productImages)
                    .contains("Bolt Cutters", "Pliers", "Hammer");
        }

    }

    @DisplayName("Using PlayWright Automatic Wait (Implicit Wait)")
    @Nested
    class AutomaticWait {
        @BeforeEach
        void setUp(Page page, Playwright playwright) {
            page.navigate("https://practicesoftwaretesting.com/");
            playwright.selectors()
                    .setTestIdAttribute("data-test");
        }

        @DisplayName("Should wait for the filter checkbox options to appear before clicking Screwdriver")
        @Test
        void shouldWaitForTheFilterCheckboxes(Page page) {
            var screwdrivers = page.getByLabel("Screwdriver");
            screwdrivers.check();
            assertThat(screwdrivers)
                    .isChecked();

        }

        @DisplayName("Should filter products by category")
        @Test
        void shouldFilterProductsByCategory(Page page) {
            page.getByRole(AriaRole.MENUBAR)
                    .getByText("Categories")
                    .click();
            page.getByRole(AriaRole.MENUITEM)
                    .getByText("Power Tools")
                    .click();
//            page.waitForSelector(".card");
            page.waitForSelector(".card",
                    new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(20000));
            var filteredProducts = page.getByTestId("product-name")
                    .allInnerTexts();
            Assertions.assertThat(filteredProducts)
                    .contains("Sheet Sander", "Belt Sander", "Random Orbit Sander");
        }
    }

    @DisplayName("Wait for Elements to appears and disappear")
    @Nested
    class WaitingForElementsToAppearAndDisapepar {
        @BeforeEach
        void setUp(Page page, Playwright playwright) {
            page.navigate("https://practicesoftwaretesting.com/");
            playwright.selectors()
                    .setTestIdAttribute("data-test");
        }

        @DisplayName("It should display a toaster message when an item is added to the cart")
        @Test
        void shouldDisplayToasterMessage(Page page){
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to Cart").click();
            assertThat(page.getByRole(AriaRole.ALERT))
                    .isVisible();
            assertThat(page.getByRole(AriaRole.ALERT))
                    .hasText("Product added to shopping cart.");

            page.waitForCondition(() -> page.getByRole(AriaRole.ALERT).isHidden());
        }

        @DisplayName("It should update the cart item count")
        @Test
        void shouldUpdateTheCartItemCount(Page page){
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to Cart").click();
            page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("1"));
//            page.waitForSelector("[data-test=cart-quantity]:has-text('1')");
            assertThat(page.getByTestId("cart-quantity"))
                    .hasText("1");
        }
    }

    @DisplayName("Wait for API calls")
    @Nested
    class WaitingForAPICalls{
        @BeforeEach
        void setUp(Page page, Playwright playwright) {
            page.navigate("https://practicesoftwaretesting.com/");
            playwright.selectors()
                    .setTestIdAttribute("data-test");
        }
        @Test
        void sortByDescendingPrice(Page page){

            //https://api.practicesoftwaretesting.com/products?sort=price,desc&between=price,1,100&page=0
            // wait for api to response when we select the sort method
            page.waitForResponse("**/products?sort**",
                    () ->{
                        page.getByTestId("sort")
                                .selectOption("Price (High - Low)");
                    });
            /*page.getByTestId("sort")
                    .selectOption("Price (High - Low)");
            page.getByTestId("product-price").first().waitFor();*/

            var productPrices = page.getByTestId("product-price")
                    .allInnerTexts()
                    .stream()
                    .map(WaitingForAPICalls::extractPrice)
                    .toList();

            Assertions.assertThat(productPrices)
                    .isNotEmpty()
                    .isSortedAccordingTo(Comparator.reverseOrder());
            System.out.println("ProductPrices: " + productPrices);
        }
        private static double extractPrice(String price){
            return Double.parseDouble(price.replace("$", ""));
        }
    }

}
