package com.player.tests;

import com.player.assertions.PlayerAssertions;
import com.player.core.BaseTest;
import com.player.mapper.PlayerMapper;
import com.player.model.Player;
import com.player.testsupport.TestGroups;
import com.player.utils.PlayerDataProvider;
import com.player.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Player API")
@Feature("Get Player")
public class GetPlayerTest extends BaseTest {

    @Story("GET01 - Get player by id returns correct player")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "GET-01 - Should return player by id",
            groups = {TestGroups.SMOKE})
    public void shouldReturnPlayerById() {
        Player created = createUser();
        Player actual = playerService.getPlayer(created.getId());
        PlayerAssertions.assertPlayerEquals(created, actual);
    }

    @Link(name = "BUG-GET02: non-existing player id returns 200 instead of 404", type = "issue")
    @Story("GET02 - Get player with invalid id returns 404")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "GET-02 - Should return 404 for invalid player id",
            dataProvider = "invalidPlayerIds",
            dataProviderClass = PlayerDataProvider.class,
            groups = {TestGroups.NEGATIVE, TestGroups.BUG})
    public void shouldReturn404ForInvalidId(long playerId) {
        Response response = playerClient.getPlayer(PlayerMapper.toGetRequest(playerId));
        Assert.assertEquals(response.statusCode(), 404,
                "invalid player id should return 404");
    }

    @Link(name = "BUG-GET03: Deleted player returns 200 with empty body instead of 404", type = "issue")
    @Story("GET03 - Get deleted player returns 404")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "GET03 - Should return 404 for deleted player",
            groups = {TestGroups.NEGATIVE, TestGroups.BUG})
    public void shouldReturn404ForDeletedPlayer() {
        Player created = createUser();
        playerService.deletePlayer(supervisor, created.getId());
        Response response = playerClient.getPlayer(PlayerMapper.toGetRequest(created.getId()));
        Assert.assertEquals(response.statusCode(), 404,
                "deleted player should return 404");
    }

    @Story("GET04 - GET player is idempotent")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "GET04 - Multiple GET requests for the same player should return identical results",
            groups = {TestGroups.SMOKE})
    public void getPlayerShouldBeIdempotent() {
        Player created = createUser();
        Player first = playerService.getPlayer(created.getId());
        Player second = playerService.getPlayer(created.getId());
        Player third = playerService.getPlayer(created.getId());

        SoftAssert soft = new SoftAssert();
        PlayerAssertions.assertPlayerEquals(first, second, soft);
        PlayerAssertions.assertPlayerEquals(first, third, soft);
        soft.assertAll();
    }

    @Link(name = "BUG-GET05: Password is returned in plaintext in get player response", type = "issue")
    @Story("GET05 - Get player response should return hashed password")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "GET05 - Password should be hashed in get player response",
            groups = {TestGroups.NEGATIVE, TestGroups.BUG})
    public void getPlayerResponseShouldReturnHashedPassword() {
        String originalPassword = TestDataGenerator.randomPassword();
        Player playerData = TestDataGenerator.validPlayerBuilder()
                .password(originalPassword)
                .build();
        Player created = createPlayer(playerData);
        Player actual = playerService.getPlayer(created.getId());

        SoftAssert soft = new SoftAssert();
        soft.assertNotNull(actual.getPassword(), "password should be present in response");
        soft.assertNotEquals(actual.getPassword(), originalPassword,
                "password should be hashed, not returned in plaintext");
        soft.assertAll();
    }
}
