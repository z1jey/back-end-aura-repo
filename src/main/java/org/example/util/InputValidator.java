package org.example.util;
import java.util.List;
import java.math.BigDecimal;

public class InputValidator {

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    public static boolean isListNotEmpty(List<?> list) {
        return list != null && !list.isEmpty();
    }
    public static boolean isContactNumberValid(String contactNumber) {
        contactNumber = contactNumber.replace("-", "").replace(" ", "").trim();

        for(int check = 0; check < contactNumber.length(); check++) {
            if(!Character.isDigit(contactNumber.charAt(check))){
                System.out.println("[ERROR] Contact Number should only contain numbers");
                return false;
            }
        }
        if(!contactNumber.startsWith("0")){
            System.out.println("[ERROR] Contact Number must start with 0");
            return false;
        }
        if(contactNumber.length() != 11) {
            System.out.println("[ERROR] Contact number must be 11 digits");
            return false;
        }
        return true;
    }

    public static boolean isAmountValid(String amount) {
        try {
            BigDecimal bigAmount = new BigDecimal(amount);
            if (bigAmount.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("[ERROR] Amount cannot be negative.");
                return false;
            }
        } catch (NumberFormatException exception) {
            System.out.println("[ERROR] Invalid amount: " + amount);
            return false;
        }
        return true;
    }

    public static boolean isPositiveAmount(String amount){
        try {
            BigDecimal bigDecimal = new BigDecimal(amount);
            if(bigDecimal.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("[ERROR] The amount must be greater that zero. ");
                return false;
            }
        } catch (NumberFormatException exception) {
            System.out.println("[ERROR] Invalid Amount: " + amount);
            return false;
        }
        return true;
    }


}
