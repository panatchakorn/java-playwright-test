package org.example.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;

public class ConcurrentTest {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;
    Page page;

    @BeforeAll
    public static void setUpBrowser() {

        playwright = Playwright.create();
        browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--start-maximized", "--incognito", "--disable-extensions", "--disable-gpu"))
                );
        browserContext = browser.newContext();

    }
    @BeforeEach
    void setUp() {
        page = browserContext.newPage();
    }

    @AfterAll
    public static void tearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    void shouldShowThePageTitleTest() {
        page.navigate("https://practicesoftwaretesting.com/");
        String title = page.title();
        Assertions.assertEquals("Practice Software Testing - Toolshop - v5.0", title);
    }

    @Test
    void shouldSearchByKeyword() {
        page.navigate("https://practicesoftwaretesting.com/");
        page.locator("[placeholder='Search']")
                .fill("pliers");
        page.locator("button:has-text('Search')")
                .click();
        int matchingSearchResults = page.locator(".card")
                .count();
        Assertions.assertTrue(matchingSearchResults > 0);
    }
}
