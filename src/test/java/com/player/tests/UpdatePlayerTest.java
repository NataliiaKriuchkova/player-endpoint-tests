package com.player.tests;

import com.player.config.Config;
import com.player.core.BaseTest;
import com.player.model.request.UpdatePlayerRequest;
import com.player.model.response.CreatePlayerResponse;
import com.player.model.response.UpdatePlayerResponse;
import com.player.utils.TestDataGenerator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UpdatePlayerTest extends BaseTest {

    @Test
    public void shouldUpdatePlayer() {
        Response createResponse = playerClient.createPlayer(
                Config.getSupervisorLogin(),
                TestDataGenerator.validPlayer()
        );
        CreatePlayerResponse created = createResponse.as(CreatePlayerResponse.class);
        Assert.assertEquals(createResponse.statusCode(), 200);
        Assert.assertNotNull(created.getId());
        registerPlayer(created.getId());

        UpdatePlayerRequest updateRequest = UpdatePlayerRequest.builder()
                .age(30)
                .screenName("UpdatedScreen30")
                .build();

        Response updateResponse = playerClient.updatePlayer(
                Config.getSupervisorLogin(),
                created.getId(),
                updateRequest
        );
        updateResponse.then().log().all();

        UpdatePlayerResponse updated = updateResponse.as(UpdatePlayerResponse.class);

        Assert.assertEquals(updateResponse.statusCode(), 200);
        Assert.assertEquals(updated.getAge(), Integer.valueOf(30));
        Assert.assertEquals(updated.getScreenName(), "UpdatedScreen30");
    }
}
