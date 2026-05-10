package edu.at.kolex.utils;

import android.text.TextUtils;

public final class ValidationUtils {
    private ValidationUtils(){}
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isMatchPassword(CharSequence password, CharSequence passwordConfirm){
        return password.equals(passwordConfirm);
    }
}
