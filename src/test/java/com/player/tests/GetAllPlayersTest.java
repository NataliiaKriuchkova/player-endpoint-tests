package com.player.tests;

import com.player.core.BaseTest;
import com.player.model.response.PlayerItem;
import com.player.testsupport.TestGroups;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Player API")
@Feature("Get All Players")
public class GetAllPlayersTest extends BaseTest {

    @Story("GETALL-01 - Get all players returns non-empty list")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "GETALL-01 - Should return all players with non-empty list",
            groups = {TestGroups.SMOKE})
    public void shouldReturnAllPlayers() {
        List<PlayerItem> players = playerService.getAllPlayers();
        Assert.assertFalse(players.isEmpty(), "players list should not be empty");
    }
}
