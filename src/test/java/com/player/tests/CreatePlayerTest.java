package com.player.tests;

import com.player.config.Config;
import com.player.core.BaseTest;
import com.player.model.request.CreatePlayerRequest;
import com.player.model.response.CreatePlayerResponse;
import com.player.utils.TestDataGenerator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreatePlayerTest extends BaseTest {

    @Test
    public void shouldCreatePlayer() {
        CreatePlayerRequest request = TestDataGenerator.validPlayer();

        Response response = playerClient.createPlayer(Config.getSupervisorLogin(), request);
        response.then().log().all();

        CreatePlayerResponse created = response.as(CreatePlayerResponse.class);
        registerPlayer(created.getId());

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(created.getId());
        Assert.assertEquals(created.getLogin(), request.getLogin());
    }
}
