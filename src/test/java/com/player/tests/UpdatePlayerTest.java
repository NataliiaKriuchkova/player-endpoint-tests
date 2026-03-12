package com.player.tests;

import com.player.assertions.PlayerAssertions;
import com.player.core.BaseTest;
import com.player.model.Player;
import com.player.model.request.UpdatePlayerRequest;
import com.player.testsupport.TestGroups;
import com.player.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Player API")
@Feature("Update Player")
public class UpdatePlayerTest extends BaseTest {

    @Story("UPD01 - Supervisor updates all allowed fields")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "UPD01 - Should update age, gender, login, screenName",
            groups = {TestGroups.SMOKE})
    public void shouldUpdateAllFields() {
        Player created = createUser();
        Player updatedData = created.toBuilder()
                .age(30)
                .gender("female")
                .login(TestDataGenerator.randomLogin())
                .screenName(TestDataGenerator.randomScreenName())
                .build();
        Player updated = playerService.updatePlayer(supervisor, updatedData);
        PlayerAssertions.assertPlayerEquals(updatedData, updated);
    }

    @Story("UPD02 - Partial update changes only sent field")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "UPD02 - Should update only screenName, other fields unchanged",
            groups = {TestGroups.SMOKE})
    public void shouldUpdateOnlyScreenName() {
        Player created = createUser();
        String newScreenName = TestDataGenerator.randomScreenName();
        UpdatePlayerRequest request = UpdatePlayerRequest.builder()
                .screenName(newScreenName)
                .build();
        Player updated = playerService.updatePlayer(supervisor, created.getId(), request);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(updated.getScreenName(), newScreenName,
                "screenName should be updated");
        soft.assertEquals(updated.getLogin(), created.getLogin(),
                "login should not change");
        soft.assertEquals(updated.getAge(), created.getAge(),
                "age should not change");
        soft.assertEquals(updated.getGender(), created.getGender(),
                "gender should not change");
        soft.assertAll();
    }

    @Story("UPD03 - Update password")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "UPD03 - Should update player password",
            groups = {TestGroups.SMOKE})
    public void shouldUpdatePassword() {
        String originalPassword = TestDataGenerator.randomPassword();
        Player created = playerService.createPlayer(supervisor,
                TestDataGenerator.validPlayerBuilder()
                        .password(originalPassword)
                        .build());
        String newPassword = TestDataGenerator.randomPassword();
        UpdatePlayerRequest request = UpdatePlayerRequest.builder()
                .password(newPassword)
                .build();
        playerService.updatePlayer(supervisor, created.getId(), request);
        Player actual = playerService.getPlayer(created.getId());

        Assert.assertNotEquals(actual.getPassword(), originalPassword,
                "password should be changed after update");
        Assert.assertEquals(actual.getPassword(), newPassword,
                "password should match new value");
    }

    @Link(name = "BUG-UPD04: User can update another user", type = "issue")
    @Story("UPD04 - User cannot update another user")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "UPD04 - User should not be allowed to update another user",
            groups = {TestGroups.NEGATIVE, TestGroups.BUG})
    public void userShouldNotUpdateAnotherUser() {
        Player user1 = createUser();
        Player user2 = createUser();
        UpdatePlayerRequest request = UpdatePlayerRequest.builder()
                .screenName(TestDataGenerator.randomScreenName())
                .build();
        Response response = playerClient.updatePlayer(
                user1.getLogin(),
                user2.getId(),
                request);

        Assert.assertEquals(response.statusCode(), 403,
                "user should not be allowed to update another user");
        Player actual = playerService.getPlayer(user2.getId());
        PlayerAssertions.assertPlayerEquals(user2, actual);
    }

    @Link(name = "BUG-UPD05: Duplicate login returns undocumented 409 Conflict", type = "issue")
    @Story("UPD05 - Update to duplicate login should be rejected")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "UPD05 - Should reject update when login already exists with 403, returns undocumented 409",
            groups = {TestGroups.NEGATIVE, TestGroups.BUG})
    public void shouldRejectDuplicateLogin() {
        Player player1 = createUser();
        Player player2 = createUser();
        UpdatePlayerRequest request = UpdatePlayerRequest.builder()
                .login(player1.getLogin())
                .build();
        Response response = playerClient.updatePlayer(
                supervisor,
                player2.getId(),
                request);
        Assert.assertEquals(response.statusCode(), 403,
                "duplicate login should be rejected with 403, but returns undocumented 409");
    }
}
