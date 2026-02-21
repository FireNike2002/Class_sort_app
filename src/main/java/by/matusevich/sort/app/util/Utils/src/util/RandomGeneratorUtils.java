package util;

import java.util.Random;

public final class RandomGeneratorUtils {
    private static final Random random = new Random();
    private static final String[] NAMES = {"User1", "User2", "User3", "User4", "User5"};

    private RandomGeneratorUtils() {}

    public static String generateName() {
        return NAMES[random.nextInt(NAMES.length)];
    }

    public static String generateEmail() {
        return "user" + random.nextInt(9999) + "@mail.com";
    }

    public static String generatePassword() {
        return "pass" + (1000 + random.nextInt(9000));
    }
}