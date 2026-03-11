package com.player.assertions;

import com.player.model.Player;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public final class PlayerAssertions {

    private static final Logger log = LogManager.getLogger(PlayerAssertions.class);

    private PlayerAssertions() {
    }

    public static void assertSuccessResponse(Response response) {
        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(
                response.header("Content-Type").contains("application/json"),
                "Content-Type should be application/json"
        );
    }

    public static void assertPlayerEquals(Player expected, Player actual) {
        log.info("Expected player: {}", expected);
        log.info("Actual player: {}", actual);

        SoftAssert soft = new SoftAssert();
        assertPlayerEquals(expected, actual, soft);
        soft.assertAll();
    }

    public static void assertPlayerEquals(Player expected, Player actual, SoftAssert soft) {

        soft.assertNotNull(actual, "actual player is null");

        if (actual != null) {
            soft.assertEquals(actual.getId(), expected.getId(), "id mismatch");
            soft.assertEquals(actual.getLogin(), expected.getLogin(), "login mismatch");
            soft.assertEquals(actual.getAge(), expected.getAge(), "age mismatch");
            soft.assertEquals(actual.getGender(), expected.getGender(), "gender mismatch");
            soft.assertEquals(actual.getRole(), expected.getRole(), "role mismatch");
            soft.assertEquals(actual.getScreenName(), expected.getScreenName(), "screenName mismatch");
        }
    }
}
