package com.noteit.auth.authentication;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public final class AuthUtil
{
    private static final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public static PasswordEncoder getEncoder()
    {
        return encoder;
    }
}
