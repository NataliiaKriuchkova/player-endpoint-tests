package com.player.tests;

import com.player.assertions.PlayerAssertions;
import com.player.core.BaseTest;
import com.player.model.Player;
import com.player.testsupport.TestGroups;
import io.qameta.allure.*;
import org.testng.annotations.Test;

@Epic("Player API")
@Feature("Get Player")
public class GetPlayerTest extends BaseTest {

    @Story("GET-01 - Get player by id returns correct player")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "GET-01 - Should return player by id",
            groups = {TestGroups.SMOKE})
    public void shouldReturnPlayerById() {
        Player created = createUser();
        Player actual = playerService.getPlayer(created.getId());
        PlayerAssertions.assertPlayerEquals(created, actual);
    }
}
