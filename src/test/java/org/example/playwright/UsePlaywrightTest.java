package org.example.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.microsoft.playwright.junit.UsePlaywright;
import org.example.playwright.browser.ChromiumBrowser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@UsePlaywright(ChromiumBrowser.class)
public class UsePlaywrightTest {
    /*public static class CustomOptions implements OptionsFactory {
        @Override
        public Options getOptions() {
            return new Options()
                    .setHeadless(false)
                    .setBrowserName("chromium")
                    .setLaunchOptions(new BrowserType.LaunchOptions()
                            .setArgs(Arrays.asList("--no-sandbox", "--start-maximized", "--incognito"))
                    );
                   *//* .setContextOptions(new Browser.NewContextOptions()
                            .setBaseURL("https://practicesoftwaretesting.com/"))
                    .setApiRequestOptions(new APIRequest.NewContextOptions()
                            .setBaseURL("https://playwright.dev"));*//*
        }
    }*/

    // Can also pass in Playwright playwright, Browser browser, BrowserContext browserContext
    @Test
    void shouldShowThePageTitleTest(Page page) {
        page.navigate("https://practicesoftwaretesting.com/");
        String title = page.title();
        Assertions.assertEquals("Practice Software Testing - Toolshop - v5.0", title);
    }

}
