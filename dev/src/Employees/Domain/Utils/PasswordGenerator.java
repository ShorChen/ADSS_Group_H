package Employees.Domain.Utils;

import java.util.*;

public class PasswordGenerator {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_=+[]{}|;:,.<>?";
    private static final String ALL_CHARS = UPPER + LOWER + DIGITS + SPECIAL;

    private static final Random random = new Random();

    public static String generatePassword() {
        int length = 8;

        StringBuilder password = new StringBuilder();
        password.append(UPPER.charAt(random.nextInt(UPPER.length())));
        password.append(LOWER.charAt(random.nextInt(LOWER.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        return shuffleString(password.toString());
    }

    public static boolean validatePassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean digit = false, upper = false, lower = false, special = false;
        for (int i = 0; i < password.length(); i++) {
            CharSequence c = password.charAt(i) + "";
            if (DIGITS.contains(c)) digit = true;
            else if (UPPER.contains(c)) upper = true;
            else if (LOWER.contains(c)) lower = true;
            else if (SPECIAL.contains(c)) special = true;
            else throw new IllegalArgumentException("Password contains illegal characters");
        }
        return digit && upper && lower && special;
    }

    private static String shuffleString(String input) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < input.length(); i++)
            indices.add(i);
        Collections.shuffle(indices);
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer index : indices)
            stringBuilder.append(input.charAt(index));
        return stringBuilder.toString();

    }

}