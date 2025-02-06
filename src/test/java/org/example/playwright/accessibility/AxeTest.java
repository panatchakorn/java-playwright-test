package org.example.playwright.accessibility;

import com.deque.html.axecore.results.AxeResults;
import org.example.playwright.toolshop.fixtures.AxeTestFixtures;
import org.example.playwright.toolshop.login.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AxeTest extends AxeTestFixtures {

    private AxeResults accessibilityResults;
    private String testName;

    @DisplayName("Homepage")
    @Test
    void homepageAxeTest() {
        testName = "Homepage";
        page.navigate("https://practicesoftwaretesting.com/");
        accessibilityResults = makeAxeBuilder()
//                .include("#specific-element-under-test")
                .analyze();
        assertEquals(Collections.emptyList(), accessibilityResults.getViolations());
    }

    @DisplayName("Login Page")
    @Test
    void loginPageAxeTest() {
        testName = "Login Page";
        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        accessibilityResults = makeAxeBuilder()
//                .include("#specific-element-under-test")
                .analyze();
        assertEquals(Collections.emptyList(), accessibilityResults.getViolations());
    }

    @AfterEach
    void afterEachTest() {
        printResults(accessibilityResults, testName);
        createReport(accessibilityResults, testName);
    }

}

