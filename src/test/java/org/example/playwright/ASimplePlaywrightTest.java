package org.example.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.microsoft.playwright.Playwright;

import java.util.Arrays;

public class ASimplePlaywrightTest {
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeEach
    void setUp() {

        playwright = Playwright.create();
        browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--start-maximized", "--incognito", "--disable-extensions", "--disable-gpu"))
                );
        page = browser.newPage();
    }

    @AfterEach
    void tearDown() {
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
