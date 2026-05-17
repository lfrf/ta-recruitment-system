package com.group27.tarecruitment.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordUtil {
    private static final String SHA256_PREFIX = "sha256$";

    private PasswordUtil() {
    }

    public static boolean matches(String rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null) {
            return false;
        }
        if (storedPassword.startsWith(SHA256_PREFIX)) {
            return storedPassword.equals(hashWithPrefix(rawPassword));
        }
        return storedPassword.equals(rawPassword);
    }

    public static String hashWithPrefix(String rawPassword) {
        if (rawPassword == null) {
            return "";
        }
        return SHA256_PREFIX + sha256Hex(rawPassword);
    }

    private static String sha256Hex(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder(hashed.length * 2);
            for (byte item : hashed) {
                builder.append(String.format("%02x", item));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available.", exception);
        }
    }
}
