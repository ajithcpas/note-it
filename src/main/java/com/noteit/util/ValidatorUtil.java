package com.noteit.util;

import java.util.regex.Pattern;

public final class ValidatorUtil {
    private ValidatorUtil() {
    }

    public static boolean isValidEmail(String email) {
        final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        return !StringUtil.isBlank(email) && emailPattern.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return !StringUtil.isBlank(password) && password.length() > 5 && password.length() < 26;
    }
}
