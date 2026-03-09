package com.player.tests;

import com.player.core.BaseTest;
import com.player.model.response.GetAllPlayersResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetAllPlayersTest extends BaseTest {

    @Test
    public void shouldReturnAllPlayers() {
        Response response = playerClient.getAllPlayers();
        response.then().log().all();

        Assert.assertEquals(response.statusCode(), 200);

        GetAllPlayersResponse body = response.as(GetAllPlayersResponse.class);
        Assert.assertNotNull(body.getPlayers());
        Assert.assertFalse(body.getPlayers().isEmpty());
    }
}
