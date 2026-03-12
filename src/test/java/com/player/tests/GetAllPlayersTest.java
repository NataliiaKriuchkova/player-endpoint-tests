package com.player.tests;

import com.player.core.BaseTest;
import com.player.model.Player;
import com.player.model.response.PlayerItem;
import com.player.testsupport.TestGroups;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Epic("Player API")
@Feature("Get All Players")
public class GetAllPlayersTest extends BaseTest {

    @Story("GETALL01 - Get all players returns non-empty list")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "GETALL-01 - Should return all players with non-empty list",
            groups = {TestGroups.SMOKE})
    public void shouldReturnAllPlayers() {
        List<PlayerItem> players = playerService.getAllPlayers();
        Assert.assertFalse(players.isEmpty(), "players list should not be empty");
    }

    @Story("GETALL02 - Player list contains created player")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "GETALL-02 - Created player should appear in get all players response",
            groups = {TestGroups.SMOKE})
    public void shouldContainCreatedPlayer() {
        Player created = createUser();
        List<PlayerItem> players = playerService.getAllPlayers();
        boolean found = players.stream()
                .anyMatch(p -> p.getId() != null && p.getId().equals(created.getId()));
        Assert.assertTrue(found, "created player should be present in getAllPlayers response");
    }

    @Story("GETALL03 - Player ids in list must be unique")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "GETALL-03 - All player ids in the list should be unique",
            groups = {TestGroups.SMOKE})
    public void playerIdsShouldBeUnique() {
        List<PlayerItem> players = playerService.getAllPlayers();
        List<Long> ids = players.stream()
                .map(PlayerItem::getId)
                .collect(Collectors.toList());
        Set<Long> uniqueIds = ids.stream().collect(Collectors.toSet());
        Assert.assertEquals(ids.size(), uniqueIds.size(),
                "player ids should be unique in getAllPlayers response");
    }

    @Link(name = "BUG-GETALL04: role field is missing in getAllPlayers response", type = "issue")
    @Story("GETALL04 - Player schema validation")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "GETALL-04 - Created player should have all required fields in getAllPlayers response",
            groups = {TestGroups.SMOKE, TestGroups.BUG})
    public void createdPlayerShouldHaveAllFieldsInList() {
        Player created = createUser();
        List<PlayerItem> players = playerService.getAllPlayers();
        PlayerItem item = players.stream()
                .filter(p -> p.getId() != null && p.getId().equals(created.getId()))
                .findFirst()
                .orElse(null);

        SoftAssert soft = new SoftAssert();
        soft.assertNotNull(item, "created player should be present in getAllPlayers response");
        if (item != null) {
            soft.assertNotNull(item.getAge(), "age should not be null");
            soft.assertNotNull(item.getGender(), "gender should not be null");
            soft.assertNotNull(item.getId(), "id should not be null");
            soft.assertNotNull(item.getRole(), "role should not be null");
            soft.assertNotNull(item.getScreenName(), "screenName should not be null");
        }
        soft.assertAll();
    }
}
