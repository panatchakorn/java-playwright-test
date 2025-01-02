package org.example.playwright.browser;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;

import java.util.Arrays;

public class ChromiumBrowser implements OptionsFactory {
    @Override
    public Options getOptions() {
        return new Options()
                .setHeadless(false)
                .setBrowserName("chromium")
                .setLaunchOptions(new BrowserType.LaunchOptions()
                        .setChannel("msedge")
                        .setArgs(Arrays.asList("--no-sandbox", "--start-maximized", "--incognito"))
                );
    }
}
