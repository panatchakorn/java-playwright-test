package org.example.playwright.toolshop.fixtures;

import com.microsoft.playwright.Page;

public interface TakeFinalScreenshot {

    static void takeScreenshot(Page page, String name){
        ScreenshotManager.takeScreenshot(page, name);
    }
}
