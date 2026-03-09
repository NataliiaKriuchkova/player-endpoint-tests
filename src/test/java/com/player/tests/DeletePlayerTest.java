package com.player.tests;

import com.player.config.Config;
import com.player.core.BaseTest;
import com.player.model.request.DeletePlayerRequest;
import com.player.model.request.GetPlayerRequest;
import com.player.model.response.CreatePlayerResponse;
import com.player.utils.TestDataGenerator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeletePlayerTest extends BaseTest {

    @Test
    public void shouldDeletePlayer() {
        Response createResponse = playerClient.createPlayer(
                Config.getSupervisorLogin(),
                TestDataGenerator.validPlayer()
        );
        CreatePlayerResponse created = createResponse.as(CreatePlayerResponse.class);
        Assert.assertEquals(createResponse.statusCode(), 200);
        Assert.assertNotNull(created.getId());

        Response deleteResponse = playerClient.deletePlayer(
                Config.getSupervisorLogin(),
                new DeletePlayerRequest(created.getId())
        );
        deleteResponse.then().log().all();
        Assert.assertEquals(deleteResponse.statusCode(), 204);

        // BUG: returns 200 with empty body instead of 404 for deleted player
        Response getResponse = playerClient.getPlayer(new GetPlayerRequest(created.getId()));
        getResponse.then().log().all();
        Assert.assertEquals(getResponse.statusCode(), 404);
    }
}
