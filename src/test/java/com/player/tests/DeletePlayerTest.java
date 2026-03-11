package com.player.tests;

import com.player.core.BaseTest;
import com.player.mapper.PlayerMapper;
import com.player.model.Player;
import com.player.testsupport.TestGroups;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Player API")
@Feature("Delete Player")
public class DeletePlayerTest extends BaseTest {

    @Story("DEL-01 - Supervisor deletes player")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "DEL-01 - Should delete player successfully",
            groups = {TestGroups.SMOKE})
    public void shouldDeletePlayer() {
        Player created = createUser();
        playerService.deletePlayer(supervisor, created.getId());
        Assert.assertFalse(playerService.playerExistsById(created.getId()),
                "deleted player should not exist in getAllPlayers");
    }

    @Link(name = "BUG-DEL02: Deleted player returns 200 with empty body instead of 404", type = "issue")
    @Story("DEL-02 - Get deleted player returns 404")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "DEL-02 - Should return 404 for deleted player",
            groups = {TestGroups.BUG})
    public void shouldReturn404ForDeletedPlayer() {
        Player created = createUser();
        playerService.deletePlayer(supervisor, created.getId());
        Response response = playerClient.getPlayer(PlayerMapper.toGetRequest(created.getId()));
        Assert.assertEquals(response.statusCode(), 404,
                "deleted player should return 404");
    }
}
