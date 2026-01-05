package framework.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class RandomUtils {

    private RandomUtils() {}

    public static String generateCustomRandomString(int length, boolean useLetters, boolean useNumbers) {
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    public static String generateRandomNumericString(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    /*
     -- 'SecureRandom.getInstance("NativePRNGNonBlocking")' will be used when running tests on Linux (not supported on Windows)
     -- 'new SecureRandom()' will be used when running tests on Windows
     */
    public static int generateRandomNumber(int max) {
        SecureRandom secureRandom;

        try {
            secureRandom = SecureRandom.getInstance("NativePRNGNonBlocking");
        } catch (NoSuchAlgorithmException exc) {
            secureRandom = new SecureRandom();
        }
        return secureRandom.nextInt(max + 1);
    }

    public static int generateRandomNumber(int min, int max) {
        SecureRandom secureRandom;

        try {
            secureRandom = SecureRandom.getInstance("NativePRNGNonBlocking");
        } catch (NoSuchAlgorithmException exc) {
            secureRandom = new SecureRandom();
        }
        return secureRandom.nextInt(max - min + 1) + min;
    }

    public static String generateRandomAlphabeticString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static String generateRandomAlphanumericString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}