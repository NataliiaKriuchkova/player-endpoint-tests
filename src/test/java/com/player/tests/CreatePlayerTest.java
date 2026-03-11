package com.player.tests;

import com.player.assertions.PlayerAssertions;
import com.player.core.BaseTest;
import com.player.model.Player;
import com.player.model.response.CreatePlayerResponse;
import com.player.testsupport.TestGroups;
import com.player.utils.PlayerDataProvider;
import com.player.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Player API")
@Feature("Create Player")
public class CreatePlayerTest extends BaseTest {

    @Link(name = "BUG-SC01: Create player response returns null fields except id and login", type = "issue")
    @Story("SC01 - Response contract validation")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "SC01 - Create player response should contain all fields",
            groups = {TestGroups.BUG, TestGroups.CREATE})
    public void createPlayerResponseShouldContainAllFields() {
        Player playerData = TestDataGenerator.validPlayer();
        Response response = playerService.attemptCreatePlayer(supervisor, playerData);
        CreatePlayerResponse created = response.as(CreatePlayerResponse.class);
        registerPlayerForCleanup(created.getId());

        SoftAssert soft = new SoftAssert();
        soft.assertNotNull(created.getAge(), "age should not be null");
        soft.assertNotNull(created.getGender(), "gender should not be null");
        soft.assertNotNull(created.getRole(), "role should not be null");
        soft.assertNotNull(created.getScreenName(), "screenName should not be null");
        soft.assertAll();
    }

    @Story("SC02 - Supervisor creates valid user")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "SC02 - Supervisor creates valid user",
            groups = {TestGroups.SMOKE, TestGroups.CREATE})
    public void shouldCreateAndGetPlayer() {
        Player expectedPlayer = createUser();
        Player actualPlayer = playerService.getPlayer(expectedPlayer.getId());
        PlayerAssertions.assertPlayerEquals(expectedPlayer, actualPlayer);
    }

    @Story("SC03 - Admin creates valid user")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "SC03 - Admin creates valid user",
            groups = {TestGroups.SMOKE, TestGroups.CREATE})
    public void shouldCreateUserByAdmin() {
        Player createdAdmin = createAdmin();
        Player createdUser = createPlayer(createdAdmin.getLogin(), TestDataGenerator.validPlayer());
        Player actual = playerService.getPlayer(createdUser.getId());
        PlayerAssertions.assertPlayerEquals(createdUser, actual);
    }

    @Story("SC04 - Supervisor creates admin user")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "SC04 - Supervisor creates admin user",
            groups = {TestGroups.SMOKE, TestGroups.CREATE})
    public void shouldCreateAdminBySupervisor() {
        Player expectedAdmin = createAdmin();
        Player actualAdmin = playerService.getPlayer(expectedAdmin.getId());
        PlayerAssertions.assertPlayerEquals(expectedAdmin, actualAdmin);
    }

    @Story("SC05 - Role-based access control")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "SC05 - User should not be allowed to create another user",
            groups = {TestGroups.NEGATIVE, TestGroups.CREATE})
    public void shouldNotAllowUserToCreateAnotherUser() {
        Player user = createUser();
        Player anotherUserData = TestDataGenerator.validPlayer();
        Response response = playerService.attemptCreatePlayer(user.getLogin(), anotherUserData);
        Long createdId = playerService.findPlayerIdByAgeAndScreenName(
                anotherUserData.getAge(), anotherUserData.getScreenName());
        registerPlayerForCleanup(createdId);
        Assert.assertEquals(response.statusCode(), 403);
    }

    @Link(name = "BUG-SC06: Duplicate login is accepted and existing player data is silently overwritten",
            type = "issue")
    @Story("SC06 - Duplicate login validation")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "SC06 - Duplicate login should be rejected and existing player data should not be overwritten",
            groups = {TestGroups.NEGATIVE, TestGroups.CREATE})
    public void shouldNotAllowDuplicateLogin() {
        String duplicateLogin = TestDataGenerator.randomLogin();
        Player firstPlayerData = TestDataGenerator.validPlayerBuilder()
                .login(duplicateLogin)
                .build();
        Player firstPlayer = createPlayer(firstPlayerData);
        Player intruderData = TestDataGenerator.validPlayerBuilder()
                .login(duplicateLogin)
                .build();
        Response response = playerService.attemptCreatePlayer(supervisor, intruderData);
        registerPlayerForCleanup(playerService.findPlayerIdByScreenName(intruderData.getScreenName()));

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.statusCode(), 400, "duplicate login should be rejected with 400");
        PlayerAssertions.assertPlayerEquals(firstPlayer, playerService.getPlayer(firstPlayer.getId()), soft);
        soft.assertAll();
    }

    @Link(name = "BUG-SC07: Duplicate screenName is accepted — new player is created with duplicate screenName",
            type = "issue")
    @Story("SC07 - Duplicate screenName validation")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "SC07 - Duplicate screenName should be rejected and no new player should be created",
            groups = {TestGroups.NEGATIVE, TestGroups.CREATE})
    public void shouldNotAllowDuplicateScreenName() {
        String duplicateScreenName = TestDataGenerator.randomScreenName();
        Player firstPlayerData = TestDataGenerator.validPlayerBuilder()
                .screenName(duplicateScreenName)
                .build();
        Player firstPlayer = createPlayer(firstPlayerData);
        Player intruderData = TestDataGenerator.validPlayerBuilder()
                .screenName(duplicateScreenName)
                .build();
        Response response = playerService.attemptCreatePlayer(supervisor, intruderData);
        playerService.findPlayerIdsByScreenName(duplicateScreenName).forEach(this::registerPlayerForCleanup);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.statusCode(), 400, "duplicate screenName should be rejected with 400");
        PlayerAssertions.assertPlayerEquals(firstPlayer, playerService.getPlayer(firstPlayer.getId()), soft);
        soft.assertEquals(playerService.countPlayersByScreenName(duplicateScreenName), 1L,
                "duplicateScreenName should appear exactly once in getAllPlayers");
        soft.assertAll();
    }

    @Story("SC08 - Valid age is accepted")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "SC08 - Valid age should be accepted",
            dataProvider = "validAges",
            dataProviderClass = PlayerDataProvider.class,
            groups = {TestGroups.CREATE})
    public void shouldAllowValidAge(int age) {
        Player playerData = TestDataGenerator.validPlayerBuilder().age(age).build();
        Player createdPlayer = createPlayer(playerData);
        Player actualPlayer = playerService.getPlayer(createdPlayer.getId());
        PlayerAssertions.assertPlayerEquals(createdPlayer, actualPlayer);
    }

    @Link(name = "BUG-SC09: Age=60 is accepted — player is created with age above maximum", type = "issue")
    @Story("SC09 - Invalid age is rejected")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "SC09 - Invalid age should be rejected",
            dataProvider = "invalidAges",
            dataProviderClass = PlayerDataProvider.class,
            groups = {TestGroups.NEGATIVE, TestGroups.CREATE})
    public void shouldRejectInvalidAge(int age) {
        Player playerData = TestDataGenerator.validPlayerBuilder().age(age).build();
        Response response = playerService.attemptCreatePlayer(supervisor, playerData);

        Long createdId = playerService.findPlayerIdByAgeAndScreenName(age, playerData.getScreenName());
        registerPlayerForCleanup(createdId);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.statusCode(), 400, "invalid age should be rejected with 400");
        soft.assertNull(createdId, "player with invalid age should not exist in the system");
        soft.assertAll();
    }

    @Link(name = "BUG-SC10: Invalid password is accepted — player is created with password that fails validation",
            type = "issue")
    @Story("SC10 - Invalid password is rejected")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "SC10 - Invalid password should be rejected",
            dataProvider = "invalidPasswords",
            dataProviderClass = PlayerDataProvider.class,
            groups = {TestGroups.NEGATIVE, TestGroups.CREATE})
    public void shouldNotAllowInvalidPassword(String password) {
        Player playerData = TestDataGenerator.validPlayerBuilder().password(password).build();
        Response response = playerService.attemptCreatePlayer(supervisor, playerData);

        Long createdId = playerService.findPlayerIdByAgeAndScreenName(playerData.getAge(), playerData.getScreenName());
        registerPlayerForCleanup(createdId);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.statusCode(), 400, "invalid password should be rejected with 400");
        soft.assertNull(createdId, "player with invalid password should not exist in the system");
        soft.assertAll();
    }

    @Link(name = "BUG-SC11: Invalid gender is accepted — player is created with gender that fails validation",
            type = "issue")
    @Story("SC11 - Invalid gender is rejected")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "SC11 - Invalid gender should be rejected",
            dataProvider = "invalidGenders",
            dataProviderClass = PlayerDataProvider.class,
            groups = {TestGroups.NEGATIVE, TestGroups.CREATE})
    public void shouldNotAllowInvalidGender(String gender) {
        Player playerData = TestDataGenerator.validPlayerBuilder().gender(gender).build();
        Response response = playerService.attemptCreatePlayer(supervisor, playerData);

        Long createdId = playerService.findPlayerIdByAgeAndScreenName(playerData.getAge(), playerData.getScreenName());
        registerPlayerForCleanup(createdId);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.statusCode(), 400, "invalid gender should be rejected with 400");
        soft.assertNull(createdId, "player with invalid gender should not exist in the system");
        soft.assertAll();
    }

    @Link(name = "BUG-SC12: Uppercase role ADMIN/USER is accepted — player is created with invalid role",
            type = "issue")
    @Story("SC12 - Role validation")
    @Severity(SeverityLevel.NORMAL)
    @Test(description = "SC12 - Invalid role should be rejected",
            dataProvider = "invalidRoles",
            dataProviderClass = PlayerDataProvider.class,
            groups = {TestGroups.NEGATIVE, TestGroups.CREATE})
    public void shouldNotAllowInvalidRole(String role) {
        Player playerData = TestDataGenerator.validPlayerBuilder().role(role).build();
        Response response = playerService.attemptCreatePlayer(supervisor, playerData);

        Long createdId = playerService.findPlayerIdByAgeAndScreenName(playerData.getAge(), playerData.getScreenName());
        registerPlayerForCleanup(createdId);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(response.statusCode(), 400, "invalid role should be rejected with 400");
        soft.assertNull(createdId, "player with invalid role should not exist in the system");
        soft.assertAll();
    }
}
