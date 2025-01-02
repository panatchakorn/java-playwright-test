package org.example.playwright.toolshop.cucumber.stepdefinitions;

import com.microsoft.playwright.*;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import org.example.playwright.toolshop.fixtures.TakeFinalScreenshot;
import org.junit.jupiter.api.TestInfo;

import java.nio.file.Paths;
import java.util.Arrays;

public class PlaywrightCucumberFixtures {
    private static final ThreadLocal <Playwright> playwright = ThreadLocal.withInitial(() ->{
        Playwright playwright = Playwright.create();
        playwright.selectors()
                .setTestIdAttribute("data-test");
        return playwright;
    });
    private static final ThreadLocal <Browser> browser = ThreadLocal.withInitial(() ->
            playwright.get().chromium()
                    .launch(
                            new BrowserType.LaunchOptions().setHeadless(false)
//                                    .setChannel("chrome")
                                    .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu", "--start-maximized", "--incognito"))
                    )

    );

    private static final ThreadLocal<BrowserContext> browserContext = new ThreadLocal<>();

    private static final ThreadLocal<Page> page = new ThreadLocal<>();

    private String traceName;


    @Before(order=100)
    public void setUpBrowserContext() {


        browserContext.set(browser.get().newContext());
        // Start tracing before creating page


        page.set(browserContext.get().newPage());
    }

    @After(order=100)
    public void closeContext() {
        TakeFinalScreenshot.takeScreenshot(page.get(), traceName);

        browserContext.get().close();
    }

    @AfterAll
    public static void tearDown() {
        browser.get().close();
        browser.remove();
        playwright.get().close();
        playwright.remove();
    }

    public static Page getPage(){
        return page.get();
    }

    public static BrowserContext getBrowserContext() {
        return browserContext.get();
    }
}
