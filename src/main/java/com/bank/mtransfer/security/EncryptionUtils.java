package com.bank.mtransfer.security;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.keygen.KeyGenerators;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {
    private static final String SECRET_KEY = KeyGenerators.string().generateKey();

    public static String encrypt(String data) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.encodeBase64String(encryptedBytes);
    }

    public static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encryptedData));
        return new String(decryptedBytes);
    }
}
