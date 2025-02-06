package org.example.playwright.toolshop.fixtures;

import com.deque.html.axecore.playwright.AxeBuilder;
import com.deque.html.axecore.playwright.Reporter;
import com.deque.html.axecore.results.AxeResults;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

// https://www.deque.com/axe/core-documentation/api-documentation/#axe-core-tags

public class AxeTestFixtures extends PlaywrightTestCase {
    protected AxeBuilder makeAxeBuilder() {
        return new AxeBuilder(page)
                .withTags(Arrays.asList("wcag2a", "wcag2aa", "wcag2aaa", "wcag21a", "wcag21aa", "wcag22aa", "best-practice", "ACT"))
                .exclude("#comonly-released-element-with-known-issues");
    }

    public void printResults(AxeResults accessibilityResults, String testName) {
        System.out.println("Test Name: " + testName);
        System.out.println("Current Page URL: " + page.url());
        System.out.println("Accessibility VIOLATIONS count: " + accessibilityResults.getViolations()
                .size());
        System.out.println("Accessibility violations: " + accessibilityResults.getViolations());
        System.out.println("------------------------------------");
        System.out.println("Accessibility PASSES count: " + accessibilityResults.getPasses()
                .size());
        System.out.println("Accessibility passes: " + accessibilityResults.getPasses());
        System.out.println("------------------------------------");
        System.out.println("Accessibility INCOMPLETE count: " + accessibilityResults.getIncomplete()
                .size());
        System.out.println("Accessibility incomplete: " + accessibilityResults.getIncomplete());
        System.out.println("------------------------------------");
        System.out.println("Accessibility INAPPLICABLE count: " + accessibilityResults.getInapplicable()
                .size());
        System.out.println("Accessibility inapplicable: " + accessibilityResults.getInapplicable());
    }

    public void createReport(AxeResults accessibilityResults, String testName) {
        testName = testName.trim().replace(" ", "_").toLowerCase();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmssSSS");
        String timestamp = LocalDateTime.now()
                .format(formatter);
        String fileName = testName + "_" + timestamp + ".json";

        try {
            Path reportDir = Paths.get("target", "reports", "axe");
            if (!Files.exists(reportDir)) {
                Files.createDirectories(reportDir);
            }

            Path filePath = reportDir.resolve(fileName);
            Files.createFile(filePath);
            Reporter reporter = new Reporter();
            reporter.JSONStringify(accessibilityResults, filePath.toAbsolutePath()
                    .toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
