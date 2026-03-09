package com.player.tests;

import com.player.config.Config;
import com.player.core.BaseTest;
import com.player.model.request.GetPlayerRequest;
import com.player.model.request.CreatePlayerRequest;
import com.player.model.response.CreatePlayerResponse;
import com.player.model.response.GetPlayerResponse;
import com.player.utils.TestDataGenerator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetPlayerTest extends BaseTest {

    @Test
    public void shouldReturnPlayerById() {
        CreatePlayerRequest createRequest = TestDataGenerator.validPlayer();
        Response createResponse = playerClient.createPlayer(Config.getSupervisorLogin(), createRequest);
        CreatePlayerResponse created = createResponse.as(CreatePlayerResponse.class);
        registerPlayer(created.getId());

        Response response = playerClient.getPlayer(new GetPlayerRequest(created.getId()));
        response.then().log().all();

        GetPlayerResponse player = response.as(GetPlayerResponse.class);

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(player.getId(), created.getId());
        Assert.assertEquals(player.getLogin(), createRequest.getLogin());
    }
}
