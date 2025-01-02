package org.example.playwright.toolshop.fixtures;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.nio.file.Paths;
import java.util.Arrays;

public abstract class PlaywrightTestCase {
    protected static ThreadLocal <Playwright> playwright = ThreadLocal.withInitial(() ->{
        Playwright playwright = Playwright.create();
        playwright.selectors()
                .setTestIdAttribute("data-test");
        return playwright;
    });
    protected static ThreadLocal <Browser> browser = ThreadLocal.withInitial(() ->
        playwright.get().chromium()
                .launch(
                        new BrowserType.LaunchOptions().setHeadless(false)
                                .setChannel("chrome")
                                .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu", "--start-maximized", "--incognito"))
                )

    );

   protected BrowserContext browserContext;
    private String traceName;

    protected Page page;

   /* @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        playwright.selectors()
                .setTestIdAttribute("data-test");
        browser = playwright.chromium()
                .launch(
                        new BrowserType.LaunchOptions().setHeadless(false)
//                                .setChannel("chrome")
                                .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu", "--start-maximized", "--incognito"))
                );
    }*/

    @BeforeEach
    void setUpBrowserContext(TestInfo testInfo) {
        traceName = testInfo.getDisplayName().replaceAll(" ","_").toLowerCase();
        browserContext = browser.get().newContext();
        // Start tracing before creating page
        browserContext.tracing()
                .start(new Tracing.StartOptions().setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
                        .setName(traceName));

        page = browserContext.newPage();
    }

    @AfterEach
    void closeContext() {
        TakeFinalScreenshot.takeScreenshot(page, traceName);
        browserContext.tracing().stop(
                new Tracing.StopOptions().setPath(Paths.get("target/log/trace_" + traceName +".zip"))
        );
        browserContext.close();
    }

    @AfterAll
    static void tearDown() {
        browser.get().close();
        browser.remove();
        playwright.get().close();
        playwright.remove();
    }
}
