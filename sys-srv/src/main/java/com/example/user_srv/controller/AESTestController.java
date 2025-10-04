package com.example.user_srv.controller;

import com.example.user_srv.utils.AESHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test/aes")
@RequiredArgsConstructor
public class AESTestController {

    private final AESHelper aesHelper;

    @PostMapping("/encrypt")
    public String encrypt(@RequestParam String plainText) {
        try {
            return aesHelper.encrypt(plainText);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/decrypt")
    public String decrypt(@RequestParam String encryptedText) {
        try {
            return aesHelper.decrypt(encryptedText);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}