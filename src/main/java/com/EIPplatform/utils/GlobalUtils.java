package com.EIPplatform.utils;
import java.security.SecureRandom;
import java.util.Random;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalUtils {

    public String generateVerificationCode() {
        Random random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
