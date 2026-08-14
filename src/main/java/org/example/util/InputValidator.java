package org.example.util;

public class InputValidator {

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isContactNumberValid(String contactNumber) {
        contactNumber = contactNumber.replace("-", "").replace(" ", "");
        if(contactNumber.length() != 11) {
            System.out.println("[ERROR] Contact number must be 11 digits");
            return false;
        }
        if(!contactNumber.startsWith("0")){
            System.out.println("[ERROR] Contact Number must start with 0");
            return false;
        }
        for(int check = 0; check < contactNumber.length(); check++) {
            if(!Character.isDigit(contactNumber.charAt(check))){
                System.out.println("[ERROR] Contact Number should only contain numbers");
                return false;
            }
        }
        return true;
    }
}
