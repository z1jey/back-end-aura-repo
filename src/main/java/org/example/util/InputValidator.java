package org.example.util;

public class InputValidator {

    public static boolean isNotEmpty(String value) {
        System.out.println(value + " cannot be empty");
        return value != null && !value.trim().isEmpty();
    }


}
