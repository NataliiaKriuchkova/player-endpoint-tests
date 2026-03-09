package com.player.tests;

import com.player.client.PlayerClient;
import com.player.model.request.GetPlayerRequest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetPlayerTest {
    private final PlayerClient playerClient = new PlayerClient();

    @Test
    public void shouldReturnPlayerById() {
        GetPlayerRequest request = new GetPlayerRequest(1L);
        Response response = playerClient.getPlayer(request);
        response.then().log().all();
        Assert.assertEquals(response.statusCode(), 200);
    }
}
