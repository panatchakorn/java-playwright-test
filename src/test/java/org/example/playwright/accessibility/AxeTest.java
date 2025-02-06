package org.example.playwright.accessibility;

import com.deque.html.axecore.results.AxeResults;
import org.example.playwright.toolshop.fixtures.AxeTestFixtures;
import org.example.playwright.toolshop.login.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AxeTest extends AxeTestFixtures {

    @DisplayName("Homepage")
    @Test
    void homepageAxeTest() {
        page.navigate("https://practicesoftwaretesting.com/");
        AxeResults accessibilityResults = makeAxeBuilder()
//                .include("#specific-element-under-test")
                .analyze();
        printResults(accessibilityResults, "Homepage");
        assertEquals(Collections.emptyList(), accessibilityResults.getViolations());
    }

    @DisplayName("Login Page")
    @Test
    void loginPageAxeTest() {
        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        AxeResults accessibilityResults = makeAxeBuilder()
//                .include("#specific-element-under-test")
                .analyze();
        printResults(accessibilityResults, "Login Page");
        assertEquals(Collections.emptyList(), accessibilityResults.getViolations());
    }

}

