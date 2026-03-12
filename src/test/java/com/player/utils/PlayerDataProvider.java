package com.player.utils;

import org.testng.annotations.DataProvider;

public final class PlayerDataProvider {

    private PlayerDataProvider() {
    }

    @DataProvider(name = "invalidPasswords")
    public static Object[][] invalidPasswords() {
        return new Object[][]{
                {TestDataGenerator.tooShortPassword()},
                {TestDataGenerator.tooLongPassword()},
                {TestDataGenerator.passwordWithoutDigits()},
                {TestDataGenerator.passwordWithoutLetters()},
        };
    }

    @DataProvider(name = "validAges")
    public static Object[][] validAges() {
        return new Object[][]{
                {TestDataGenerator.MIN_AGE},
                {TestDataGenerator.MAX_AGE},
        };
    }

    @DataProvider(name = "invalidAges")
    public static Object[][] invalidAges() {
        return new Object[][]{
                {TestDataGenerator.MIN_AGE - 1},
                {TestDataGenerator.MAX_AGE + 1}
        };
    }

    @DataProvider(name = "invalidGenders")
    public static Object[][] invalidGenders() {
        return new Object[][]{
                {"unknown"},
                {"transgender"},
                {"Male"},
                {"MALE"},
                {"FEMALE"},
                {"123"},
                {""},
        };
    }

    @DataProvider(name = "invalidRoles")
    public static Object[][] invalidRoles() {
        return new Object[][]{
                {"supervisor"},
                {"guest"},
                {"ADMIN"},
                {"USER"},
                {""},
                {"123"},
        };
    }

    @DataProvider(name = "invalidPlayerIds")
    public static Object[][] invalidPlayerIds() {
        return new Object[][]{
                {-1L},
                {0L},
                {Long.MAX_VALUE}
        };
    }
}
