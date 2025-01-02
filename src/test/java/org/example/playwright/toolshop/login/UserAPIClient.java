package org.example.playwright.toolshop.login;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.RequestOptions;
import org.example.playwright.domain.User;

public class UserAPIClient {
    private final Page page;
    private static final String BASE_URL = "https://api.practicesoftwaretesting.com";
    private static final String USERS_REGISTER = "/users/register";

    public UserAPIClient(Page page) {
        this.page = page;
    }

    public void registerUser(User user) {
        var response = page.request().post(BASE_URL + USERS_REGISTER,
                RequestOptions.create()
                        .setData(user)
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept", "application/json")
        );
        if (response.status() != 201){
            throw new IllegalArgumentException("User registration failed: " + response.text());
        }
    }
}
