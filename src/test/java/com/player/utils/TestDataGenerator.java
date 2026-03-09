package com.player.utils;

import com.player.model.request.CreatePlayerRequest;

import java.util.concurrent.ThreadLocalRandom;

public final class TestDataGenerator {

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final int MIN_AGE = 17;
    private static final int MAX_AGE = 60;
    private static final int LETTER_COUNT = 5;
    private static final int DIGIT_COUNT = 3;

    private TestDataGenerator() {}

    public static CreatePlayerRequest.Builder validPlayerBuilder() {
        return new CreatePlayerRequest.Builder()
                .age(randomAge())
                .gender(randomGender())
                .login(randomLogin())
                .password(randomPassword())
                .role("user")
                .screenName(randomScreenName());
    }

    public static CreatePlayerRequest validPlayer() {
        return validPlayerBuilder().build();
    }

    public static int randomAge() {
        return ThreadLocalRandom.current().nextInt(MIN_AGE, MAX_AGE);
    }

    public static String randomGender() {
        return ThreadLocalRandom.current().nextBoolean() ? "male" : "female";
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
}
