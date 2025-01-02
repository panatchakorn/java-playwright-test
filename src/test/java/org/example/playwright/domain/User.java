package org.example.playwright.domain;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record User(
        String first_name,
        String last_name,
        String address,
        String city,
        String state,
        String country,
        String postcode,
        String phone,
        String dob,
        String password,
        String email
) {
    public static User randomUser(){
        Faker faker = new Faker();
        int year = faker.number().numberBetween(1970, 2000);
        int month = faker.number().numberBetween(1, 12);
        int day = faker.number().numberBetween(1, 28);
        LocalDate date = LocalDate.of(year, month, day);
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new User(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.address().streetAddress(),
                faker.address().city(),
                faker.address().state(),
                faker.address().country(),
                faker.address().postcode(),
                faker.phoneNumber().cellPhone(),
                formattedDate,
                "Az123!&xyz",
                faker.internet().emailAddress()
        );
    }

    public User withPassword(String password){
        return new User(first_name, last_name, address, city, state, country, postcode, phone, dob, password, email);
    }
}
