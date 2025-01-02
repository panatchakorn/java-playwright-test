package org.example.playwright;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.junit.UsePlaywright;
import org.assertj.core.api.Assertions;
import org.example.playwright.browser.ChromiumBrowser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

@UsePlaywright(ChromiumBrowser.class)
public class PlaywrightRestAPITest {
    @DisplayName("Mock API response")
    @Nested
    class MockingAPIResponses {
        @BeforeEach
        void setUp(Page page, Playwright playwright) {
            page.navigate("https://practicesoftwaretesting.com/");
            playwright.selectors()
                    .setTestIdAttribute("data-test");
        }

        @DisplayName("When a search returns a single product")
        @Test
        void whenASingleItemIsFound(Page page) {

            //https://api.practicesoftwaretesting.com/products?sort=price,desc&between=price,1,100&page=0
            // mock api response
            page.route("**/products?search?q=Pliers",
                    route -> route.fulfill(new Route.FulfillOptions()
                            .setBody(MockSearchResponses.RESPONSE_WITH_A_SINGLE_ENTRY)
                            .setStatus(200)
                    ));
            page.getByPlaceholder("Search")
                    .fill("Pliers");
            page.getByPlaceholder("Search")
                    .press("Enter");

            PlaywrightAssertions.assertThat(page.getByTestId("product-name"))
                    .hasCount(1);
            PlaywrightAssertions.assertThat(page.getByTestId("product-name"))
                    .hasText("Super Pliers");
        }

        @DisplayName("When a search returns no items")
        @Test
        void whenNoItemsAreFound(Page page) {
            // mock api response
            page.route("**/products?search?q=Pliers",
                    route -> route.fulfill(new Route.FulfillOptions()
                            .setBody(MockSearchResponses.RESPONSE_WITH_NO_ENTRIES)
                            .setStatus(200)
                    ));
            page.getByPlaceholder("Search")
                    .fill("Pliers");
            page.getByPlaceholder("Search")
                    .press("Enter");

            PlaywrightAssertions.assertThat(page.getByTestId("product-name"))
                    .hasCount(0);
            PlaywrightAssertions.assertThat(page.getByTestId("search_completed"))
                    .hasText("There are no products found.");
        }
    }

    @Nested
    class MakingAPICalls {
        record Product(String name, double price) {
        }

        private static APIRequestContext requestContext;

        @BeforeAll
        public static void setupRequestContext(Playwright playwright) {
            requestContext = playwright.request()
                    .newContext(
                            new APIRequest.NewContextOptions()
                                    .setBaseURL("https://api.practicesoftwaretesting.com")
                                    .setExtraHTTPHeaders(new HashMap<>() {{
                                        put("Accept", "application/json");
                                    }})
                    );
        }

        @DisplayName("Check presence of products")
        @ParameterizedTest(name="Checking product {0}")
        @MethodSource("products")
        void checkKnownProducts(Product product, Page page) {
            page.navigate("https://practicesoftwaretesting.com/");
            page.fill("[placeholder='Search']", product.name);
            page.click("button:has-text('Search')");

            Locator productCard = page.locator(".card")
                    .filter(
                            new Locator.FilterOptions()
                                    .setHasText(product.name)
                                    .setHasText(Double.toString(product.price))
                    );

            PlaywrightAssertions.assertThat(productCard).isVisible();

        }

        static Stream<Product> products() {
            APIResponse response = requestContext.get("/products?page=2");
            Assertions.assertThat(response.status())
                    .isEqualTo(200);
            JsonObject responseBody = new Gson().fromJson(response.text(), JsonObject.class);
            JsonArray data = responseBody.getAsJsonArray("data");

            return data.asList()
                    .stream()
                    .map(jsonElement -> {
                        JsonObject product = jsonElement.getAsJsonObject();
                        return new Product(
                                product.get("name").getAsString(),
                                product.get("price").getAsDouble());
                    });
        }
    }
}
