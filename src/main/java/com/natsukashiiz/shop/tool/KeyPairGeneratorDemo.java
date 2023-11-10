package com.natsukashiiz.shop.tool;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class KeyPairGeneratorDemo {

    public static KeyPair keyPair(int size) {
        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert generator != null;
        generator.initialize(size);
        return generator.generateKeyPair();
    }

    public static void main(String[] args) {
        // key pair
        KeyPair keyPair = KeyPairGeneratorDemo.keyPair(2048);
        System.err.println(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        System.err.println(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
    }
}