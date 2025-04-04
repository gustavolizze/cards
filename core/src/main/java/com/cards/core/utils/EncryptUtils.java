package com.cards.core.utils;

import javax.crypto.*;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class EncryptUtils {

    public static String ALGORITHM = "PBKDF2WithHmacSHA1";

    public static GCMParameterSpec generateIv() {
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        return new GCMParameterSpec(128, iv);
    }

    public static SecretKey getKey(
            String key
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        var digest = MessageDigest.getInstance("SHA-256");
        var bytes = digest.digest(key.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(bytes, "AES");
    }

    public static String encrypt(
            String input, SecretKey key
    ) throws IllegalBlockSizeException,
            BadPaddingException,
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, EncryptUtils.generateIv());
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public static String decrypt(
            String input,
            SecretKey key,
            GCMParameterSpec iv
    ) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher
                .doFinal(
                        Base64.getDecoder()
                                .decode(input)
                );
        return new String(plainText);
    }
}



