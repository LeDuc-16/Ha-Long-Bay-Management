package com.example.user_srv.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AESHelper {

    @Value("${aes.signatureKey}")
    String SIGNER_KEY;

    @Value("${aes.ivParameter}")
    String IV_KEY;

    public String encrypt(String plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SIGNER_KEY.getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV_KEY.getBytes());

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedText) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        SecretKeySpec secretKey = new SecretKeySpec(SIGNER_KEY.getBytes(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV_KEY.getBytes());

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        return new String(decryptedBytes);
    }

}
