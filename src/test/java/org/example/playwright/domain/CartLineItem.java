package org.example.playwright.domain;

public record CartLineItem(
        String title,
        int quantity,
        double price,
        double total) {
}
