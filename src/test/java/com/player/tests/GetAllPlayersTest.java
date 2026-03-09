package com.player.tests;

import com.player.client.PlayerClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetAllPlayersTest {
    private final PlayerClient playerClient = new PlayerClient();

    @Test
    public void shouldReturnAllPlayers() {
        Response response = playerClient.getAllPlayers();
        response.then().log().all();
        Assert.assertEquals(response.statusCode(), 200);
    }
}
