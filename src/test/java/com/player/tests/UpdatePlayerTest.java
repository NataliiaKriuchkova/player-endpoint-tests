package com.player.tests;

import com.player.core.BaseTest;
import com.player.model.Player;
import com.player.testsupport.TestGroups;
import com.player.utils.TestDataGenerator;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Player API")
@Feature("Update Player")
public class UpdatePlayerTest extends BaseTest {

    @Story("UPD-01 - Supervisor updates player")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "UPD-01 - Should update player age and screenName",
            groups = {TestGroups.SMOKE})
    public void shouldUpdatePlayer() {
        Player created = createUser();
        Player updatedData = created.toBuilder()
                .age(30)
                .screenName(TestDataGenerator.randomScreenName())
                .build();

        Player updated = playerService.updatePlayer(supervisor, updatedData);

        SoftAssert soft = new SoftAssert();
        soft.assertEquals(updated.getAge(), Integer.valueOf(30), "age mismatch");
        soft.assertEquals(updated.getScreenName(), updatedData.getScreenName(), "screenName mismatch");
        soft.assertAll();
    }
}
