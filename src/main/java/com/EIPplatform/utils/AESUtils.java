package com.EIPplatform.utils;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.AccessLevel;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AESUtils {

    @NonFinal
    @Value("${aes.encryptJWT}")
    String AES_ENCRYPT_JWT;

    String ALGORITHM = "AES/GCM/NoPadding";
    String KEY_ALGORITHM = "AES";
    int GCM_IV_LENGTH = 12;
    int GCM_TAG_LENGTH = 16;

    @NonFinal
    SecretKeySpec secretJWTKey;

    SecureRandom secureRandom = new SecureRandom();

    @PostConstruct
    private void initializeKey() {
        byte[] keyBytesForJWT = Base64.getDecoder().decode(AES_ENCRYPT_JWT);
        this.secretJWTKey = new SecretKeySpec(keyBytesForJWT, KEY_ALGORITHM);
    }

    public String encrypt(String data) throws Exception {
        if (data == null || data.isEmpty()) {
            return data;
        }

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        byte[] iv = new byte[GCM_IV_LENGTH];
        secureRandom.nextBytes(iv);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretJWTKey, gcmSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        byte[] result = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);

        return Base64.getUrlEncoder().encodeToString(result);
    }

    public String decrypt(String encryptedData) throws Exception {
        if (encryptedData == null || encryptedData.isEmpty()) {
            return encryptedData;
        }

        byte[] decoded = Base64.getUrlDecoder().decode(encryptedData);
        byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);
        byte[] encrypted = Arrays.copyOfRange(decoded, GCM_IV_LENGTH, decoded.length);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretJWTKey, gcmSpec);

        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}