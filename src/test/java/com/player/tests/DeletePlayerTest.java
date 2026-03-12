package com.player.tests;

import com.player.core.BaseTest;
import com.player.mapper.PlayerMapper;
import com.player.model.Player;
import com.player.testsupport.TestGroups;
import com.player.utils.PlayerDataProvider;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Player API")
@Feature("Delete Player")
public class DeletePlayerTest extends BaseTest {

    @Story("DEL01 - Supervisor deletes user")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "DEL01 - Should delete player successfully",
            groups = {TestGroups.SMOKE})
    public void shouldDeletePlayer() {
        Player created = createUser();
        Response response = playerClient.deletePlayer(supervisor, PlayerMapper.toDeleteRequest(created.getId()));
        Assert.assertEquals(response.statusCode(), 204,
                "supervisor should be able to delete user");
        Assert.assertFalse(playerService.playerExistsById(created.getId()),
                "deleted player should not exist in getAllPlayers");
    }

    @Link(name = "BUG-DEL02: User can delete another user", type = "issue")
    @Story("DEL02 - User cannot delete another user")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "DEL02 - User should not be allowed to delete another user",
            groups = {TestGroups.NEGATIVE, TestGroups.BUG})
    public void userShouldNotDeleteAnotherUser() {
        Player user1 = createUser();
        Player user2 = createUser();
        Response response = playerClient.deletePlayer(
                user1.getLogin(),
                PlayerMapper.toDeleteRequest(user2.getId()));
        Assert.assertEquals(response.statusCode(), 403,
                "user should not be allowed to delete another user");
        Assert.assertTrue(playerService.playerExistsById(user2.getId()),
                "user2 should still exist after deletion attempt");
    }

    @Story("DEL03 - Admin deletes user")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "DEL03 - Admin should be able to delete a user",
            groups = {TestGroups.SMOKE})
    public void adminShouldDeleteUser() {
        Player admin = createAdmin();
        Player user = createUser();
        Response response = playerClient.deletePlayer(
                admin.getLogin(),
                PlayerMapper.toDeleteRequest(user.getId()));
        Assert.assertEquals(response.statusCode(), 204,
                "admin should be able to delete a user");
        Assert.assertFalse(playerService.playerExistsById(user.getId()),
                "user should not exist after deletion by admin");
    }

    @Story("DEL04 - Admin can delete himself")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "DEL04 - Admin should be able to delete himself",
            groups = {TestGroups.SMOKE})
    public void adminShouldDeleteHimself() {
        Player admin = createAdmin();
        Response response = playerClient.deletePlayer(
                admin.getLogin(),
                PlayerMapper.toDeleteRequest(admin.getId()));
        Assert.assertEquals(response.statusCode(), 204,
                "admin should be able to delete himself");
        Assert.assertFalse(playerService.playerExistsById(admin.getId()),
                "admin should not exist after self-deletion");
    }

    @Link(name = "BUG-DEL05: Admin can delete another admin", type = "issue")
    @Story("DEL05 - Admin cannot delete another admin")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "DEL05 - Admin should not be allowed to delete another admin",
            groups = {TestGroups.NEGATIVE, TestGroups.BUG})
    public void adminShouldNotDeleteAnotherAdmin() {
        Player admin1 = createAdmin();
        Player admin2 = createAdmin();
        Response response = playerClient.deletePlayer(
                admin1.getLogin(),
                PlayerMapper.toDeleteRequest(admin2.getId())
        );
        Assert.assertEquals(response.statusCode(), 403,
                "admin should not be allowed to delete another admin");
        Assert.assertTrue(playerService.playerExistsById(admin2.getId()),
                "admin2 should still exist after deletion attempt");
    }

    @Story("DEL06 - Delete with invalid player id returns 403")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "DEL06 - Should return 403 for invalid player id",
            dataProvider = "invalidPlayerIds",
            dataProviderClass = PlayerDataProvider.class,
            groups = {TestGroups.NEGATIVE})
    public void shouldReturn403ForInvalidPlayerId(long playerId) {
        Response response = playerClient.deletePlayer(
                supervisor,
                PlayerMapper.toDeleteRequest(playerId)
        );
        Assert.assertEquals(response.statusCode(), 403,
                "invalid player id should return 403 on delete");
    }

    @Story("DEL07 - Delete player twice returns 403")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "DEL07 - Should return 403 when deleting already deleted player",
            groups = {TestGroups.NEGATIVE})
    public void shouldReturn403WhenDeletingTwice() {
        Player created = createUser();
        playerService.deletePlayer(supervisor, created.getId());
        Response response = playerClient.deletePlayer(
                supervisor,
                PlayerMapper.toDeleteRequest(created.getId())
        );
        Assert.assertEquals(response.statusCode(), 403,
                "second delete should return 403");
    }
}
