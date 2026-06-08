package Suppliers.Domain;

import java.util.regex.Pattern;

public class ValidationUtils {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String ISRAELI_PHONE_REGEX = "^(05\\d-?\\d{7}|0[23489]-?\\d{7})$";

    public static void validateEmail(String email) throws IllegalArgumentException {
        if (email == null || email.trim().isEmpty() || !Pattern.matches(EMAIL_REGEX, email))
            throw new IllegalArgumentException("Invalid email format. Please check and try again.");
    }

    public static void validatePhone(String phone) throws IllegalArgumentException {
        if (phone == null || phone.trim().isEmpty() || !Pattern.matches(ISRAELI_PHONE_REGEX, phone))
            throw new IllegalArgumentException("Invalid Israeli phone number. Use format: 05X-XXXXXXX");
    }
}