package com.dealwala.main.dealwala.login;

import android.util.Patterns;

public class EmailValidator {

    public static final boolean isValidEmail(String paramString) {

        boolean bool = false;
        if (paramString != null) {
            bool = Patterns.EMAIL_ADDRESS.matcher(paramString).matches();
        }
        return bool;
    }
}