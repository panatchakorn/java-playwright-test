package org.example.playwright;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.RequestOptions;
import org.assertj.core.api.SoftAssertions;
import org.example.playwright.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@UsePlaywright
public class RegisterUserTest {

    private APIRequestContext request;
    private Gson gson = new Gson();

    @BeforeEach
    void setUp(Playwright playwright) {
        request = playwright.request()
                .newContext(
                        new APIRequest.NewContextOptions()
                                .setBaseURL("https://api.practicesoftwaretesting.com/")
                );
    }

    @AfterEach
    void tearDown() {
        if(request != null){
            request.dispose();
        }
    }

    @Test
    void shouldRegisterUser() {
        User validUser = User.randomUser();
        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(validUser)
        );
//        Assertions.assertThat(response.status()).isEqualTo(201);

        String responseBody = response.text();

        User createdUser = gson.fromJson(responseBody, User.class);
        JsonObject responseObject = gson.fromJson(responseBody, JsonObject.class);

//        Assertions.assertThat(createdUser).isEqualTo(validUser.withPassword(null));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.status())
                    .as("Registration should returns 201 created status code")
                    .isEqualTo(201);

            softly.assertThat(createdUser)
                    .as("User should be created with the same data as provided without password")
                    .isEqualTo(validUser.withPassword(null));

            softly.assertThat(responseObject.has("password"))
                    .as("Password should not be returned in the response")
                    .isFalse();

            softly.assertThat(responseObject.get("id").getAsString())
                    .as("Registered user should have an ID")
                    .isNotEmpty();

            softly.assertThat(response.headers().get("content-type"))
                    .as("Content-Type header should be application/json")
                    .contains("application/json");
        });
    }

    @Test
    void firstNameIsMandatory(){
        User userWithNoName = new User("",
                "Doe",
                "123 address",
                "my city",
                "my state",
                "USA",
                "12-222",
                "123-222-3333",
                "1990-01-01","AZ123$$123","bb@123example.com"
        );
        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(userWithNoName)
        );

        JsonObject responseObject = gson.fromJson(response.text(), JsonObject.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.status())
                    .as("Registration should return 422 Unprocessed Content status code")
                    .isEqualTo(422);

            softly.assertThat(responseObject.has("first_name"))
                    .as("Response should contain first_name ")
                    .isTrue();

            softly.assertThat(responseObject.get("first_name").getAsString())
                    .as("Error message should be returned for first_name")
                    .isEqualTo("The first name field is required.");
        });
    }
}