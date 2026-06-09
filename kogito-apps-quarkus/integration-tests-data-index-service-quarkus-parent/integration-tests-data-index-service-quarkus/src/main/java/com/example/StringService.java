package com.example;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StringService {

    public String toUpperCase(String word) {
        return word.toUpperCase();
    }

    public String toLowerCase(String word) {
        return word.toLowerCase();
    }
}
