package org.example.playwright.toolshop.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

import java.nio.file.Path;

public class ContactForm {
    private final Page page;
    private Locator firstNameField;
    private Locator lastNameField;
    private Locator emailField;
    private Locator messageField;
    private Locator subjectDropdown;
    private Locator uploadField;
    private Locator sendButton;

    public ContactForm(Page page) {
        this.page = page;
        this.firstNameField = page.getByLabel("First Name");
        this.lastNameField = page.getByLabel("Last Name");
        this.emailField = page.getByLabel("Email");
        this.messageField = page.getByLabel("Message");
        this.subjectDropdown = page.getByLabel("Subject");
        this.uploadField = page.getByLabel("Attachment");
        this.sendButton = page.getByText("Send");
    }

    @Step("Fill in firstname: {firstName}")
    public void setFirstName(String firstName) {
        firstNameField.fill(firstName);
    }

    @Step("Fill in lastname: {lastName}")
    public void setLastName(String lastName) {
        lastNameField.fill(lastName);
    }

    @Step("Fill in email: {email}")
    public void setEmail(String email) {
        emailField.fill(email);
    }

    @Step("Fill in message: {message}")
    public void setMessageField(String message) {
        messageField.fill(message);
    }

    @Step("Select subject dropdown: {option}")
    public void selectSubjectDropdown(String option) {
        subjectDropdown.selectOption(option);
    }

    @Step("Set attachment: {fileToUpload}")
    public void setAttachment(Path fileToUpload) {
        page.setInputFiles("#attachment", fileToUpload);
    }

    public Locator getFirstNameField() {
        return firstNameField;
    }

    public Locator getLastNameField() {
        return lastNameField;
    }

    public Locator getEmailField() {
        return emailField;
    }

    public Locator getMessageField() {
        return messageField;
    }

    public Locator getSubjectDropdown() {
        return subjectDropdown;
    }

    public Locator getUploadField() {
        return uploadField;
    }

@Step("Submit form")
    public void submitForm() {
        sendButton.click();
    }

    public String getAlertMessage() {
        return page.locator("div[role='alert'][id='message_alert']")
                .textContent();
    }

    @Step("Get alert success message")
    public String getAlertSuccessMessage() {
        return page.locator(".alert-success")
                .textContent();
    }

    @Step("Clear form field: {fieldName}")
    public void clearForm(String fieldName) {
        page.getByLabel(fieldName)
                .clear();
    }
}
