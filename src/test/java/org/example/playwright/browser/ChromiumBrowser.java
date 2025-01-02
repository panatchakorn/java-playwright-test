package org.example.playwright.browser;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;

import java.util.Arrays;

public class ChromeBrowser implements OptionsFactory {
    @Override
    public Options getOptions() {
        return new Options()
                .setHeadless(false)
                .setBrowserName("chromium")
                .setLaunchOptions(new BrowserType.LaunchOptions()
                        .setArgs(Arrays.asList("--no-sandbox", "--start-maximized", "--incognito"))
                );
    }
    @Override
    public Options getOptions() {
        return new Options()
                .setHeadless(false)
                .setBrowserName("chromium")
                .setLaunchOptions(new BrowserType.LaunchOptions()
                        .setArgs(Arrays.asList("--no-sandbox", "--start-maximized", "--incognito"))
                );
                   /* .setContextOptions(new Browser.NewContextOptions()
                            .setBaseURL("https://practicesoftwaretesting.com/"))
                    .setApiRequestOptions(new APIRequest.NewContextOptions()
                            .setBaseURL("https://playwright.dev"));*/
    }
}
