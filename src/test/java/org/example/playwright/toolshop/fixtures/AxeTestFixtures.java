package org.example.playwright.toolshop.fixtures;

import com.deque.html.axecore.playwright.AxeBuilder;
import com.deque.html.axecore.results.AxeResults;

import java.util.Arrays;

// https://www.deque.com/axe/core-documentation/api-documentation/#axe-core-tags

public class AxeTestFixtures extends PlaywrightTestCase {
    protected AxeBuilder makeAxeBuilder() {
        return new AxeBuilder(page)
                .withTags(Arrays.asList(new String[]{"wcag2a", "wcag2aa", "wcag2aaa", "wcag21a", "wcag21aa", "wcag22aa", "best-practice", "ACT"}))
                .exclude("#comonly-released-element-with-known-issues");
    }

    public void printResults(AxeResults accessibilityResults, String testName) {
        System.out.println("Test Name: " + testName);
        System.out.println("Current Page URL: " + page.url());
        System.out.println("Accessibility VIOLATIONS count: " + accessibilityResults.getViolations().size());
        System.out.println("Accessibility violations: " + accessibilityResults.getViolations());
        System.out.println("------------------------------------");
        System.out.println("Accessibility PASSES count: " + accessibilityResults.getPasses().size());
        System.out.println("Accessibility passes: " + accessibilityResults.getPasses());
        System.out.println("------------------------------------");
        System.out.println("Accessibility INCOMPLETE count: " + accessibilityResults.getIncomplete().size());
        System.out.println("Accessibility incomplete: " + accessibilityResults.getIncomplete());
        System.out.println("------------------------------------");
        System.out.println("Accessibility INAPPLICABLE count: " + accessibilityResults.getInapplicable().size());
        System.out.println("Accessibility inapplicable: " + accessibilityResults.getInapplicable());
    }

}
