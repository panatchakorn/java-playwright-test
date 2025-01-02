package org.example.playwright.toolshop.contact;

import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.example.playwright.toolshop.fixtures.PlaywrightTestCase;
import org.example.playwright.toolshop.pageobjects.ContactForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class FormTest extends PlaywrightTestCase {
    @Nested
    class WhenInteractingWithTextFields {
        ContactForm contactForm;

        @Step("Open contact form")
        @BeforeEach
        void setup() {
            contactForm = new ContactForm(page);
            page.navigate("https://practicesoftwaretesting.com/contact");
        }


        @DisplayName("Complete the form")
        @Test
        void completeForm() throws URISyntaxException {
            contactForm.setFirstName("Sarah-Jane");
            contactForm.setLastName("Smith");
            contactForm.setEmail("sj@example.com");
            contactForm.setMessageField("I am interested in your products. Those saw and pliers look great!");
            contactForm.selectSubjectDropdown("Warranty");
//        subjectDropdown.selectOption(new SelectOption().setValue("warranty"));

            Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt")
                    .toURI());
            contactForm.setAttachment(fileToUpload);

            PlaywrightAssertions.assertThat(contactForm.getFirstNameField())
                    .hasValue("Sarah-Jane");
            PlaywrightAssertions.assertThat(contactForm.getLastNameField())
                    .hasValue("Smith");
            PlaywrightAssertions.assertThat(contactForm.getEmailField())
                    .hasValue("sj@example.com");
            PlaywrightAssertions.assertThat(contactForm.getMessageField())
                    .hasValue("I am interested in your products. Those saw and pliers look great!");
            PlaywrightAssertions.assertThat(contactForm.getSubjectDropdown())
                    .hasValue("warranty");

            String uploadedFile = contactForm.getUploadField()
                    .inputValue();
            assertThat(uploadedFile).endsWith("sample-data.txt");

            contactForm.submitForm();
            Allure.step("Verify success message");
            Assertions.assertThat(contactForm.getAlertSuccessMessage())
                    .contains("Thanks for your message! We will contact you shortly.");
        }

        @DisplayName("Mandatory Fields Test")
        @ParameterizedTest
        @ValueSource(strings = {"First Name", "Last Name", "Email", "Message"})
            // junit parameterized test need to have the value source first in the first parameter.
            // Playwright Page has to come after that.
        void mandatoryFields(String fieldName) {
            contactForm.setFirstName("Sarah-Jane");
            contactForm.setLastName("Smith");
            contactForm.setEmail("sj@example.com");
            contactForm.setMessageField("My message");
            contactForm.selectSubjectDropdown("Warranty");
            contactForm.clearForm(fieldName);
            contactForm.submitForm();

            Allure.step("Verify error message");
            var errorMessages = page.getByRole(AriaRole.ALERT)
                    .getByText(fieldName + " is required");
            PlaywrightAssertions.assertThat(errorMessages)
                    .isVisible();
        }
    }
}
