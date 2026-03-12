package com.player.utils;

import com.player.model.Gender;
import com.player.model.Player;
import com.player.model.Role;

import java.util.concurrent.ThreadLocalRandom;

public final class TestDataGenerator {

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    public static final int MIN_AGE = 17;
    public static final int MAX_AGE = 59;
    private static final int LETTER_COUNT = 5;
    private static final int DIGIT_COUNT = 3;

    private TestDataGenerator() {
    }

    public static Player.Builder validPlayerBuilder() {
        return new Player.Builder()
                .age(randomAge())
                .gender(randomGender())
                .login(randomLogin())
                .password(randomPassword())
                .role(Role.USER.value())
                .screenName(randomScreenName());
    }

    public static Player validPlayer() {
        return validPlayerBuilder().build();
    }

    public static int randomAge() {
        return ThreadLocalRandom.current().nextInt(MIN_AGE, MAX_AGE + 1);
    }

    public static String randomGender() {
        Gender[] genders = Gender.values();
        return genders[ThreadLocalRandom.current().nextInt(genders.length)].value();
    }

    public static String randomLogin() {
        return "user_" + System.currentTimeMillis() + "_" + Thread.currentThread().getId()
                + "_" + ThreadLocalRandom.current().nextInt(1000);
    }

    public static String randomPassword() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < LETTER_COUNT; i++) {
            password.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }
        for (int i = 0; i < DIGIT_COUNT; i++) {
            password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        return password.toString();
    }

    public static String randomScreenName() {
        return "screen_" + System.currentTimeMillis() + "_" + Thread.currentThread().getId()
                + "_" + ThreadLocalRandom.current().nextInt(1000);
    }

    public static String tooShortPassword() {
        return randomStringOfLength(LETTERS, 4) + randomStringOfLength(DIGITS, 2);
    }

    public static String tooLongPassword() {
        return randomStringOfLength(LETTERS, 13) + randomStringOfLength(DIGITS, 3);
    }

    public static String passwordWithoutDigits() {
        return randomStringOfLength(LETTERS, 7);
    }

    public static String passwordWithoutLetters() {
        return randomStringOfLength(DIGITS, 7);
    }

    private static String randomStringOfLength(String chars, int length) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
