package org.example.playwright.toolshop.login;

import org.assertj.core.api.Assertions;
import org.example.playwright.domain.User;
import org.example.playwright.toolshop.fixtures.PlaywrightTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class LoginWithRegisteredUserTest extends PlaywrightTestCase {

    @DisplayName("Login with registered user")
    @Test
    void loginWithRegisteredUser() {
        User user = User.randomUser();
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);

        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        loginPage.loginAs(user);
        Assertions.assertThat(loginPage.title()).isEqualTo("My account");
    }

    @DisplayName("Should reject when login registered user with invalid password")
    @Test
    void loginWithRegisteredUserWithInvalidPassword() {
        User user = User.randomUser();
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);

        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        loginPage.loginAs(user.withPassword("wrong-password"));
        Assertions.assertThat(loginPage.loginErrorMessage()).isEqualTo("Invalid email or password");
    }
}
