package com.player.tests;

import com.player.client.PlayerClient;
import com.player.model.request.CreatePlayerRequest;
import com.player.model.request.DeletePlayerRequest;
import com.player.model.response.CreatePlayerResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class CreatePlayerTest {

    private final PlayerClient playerClient = new PlayerClient();
    private Long createdPlayerId;

    @Test
    public void shouldCreatePlayer() {
        CreatePlayerRequest request = CreatePlayerRequest.builder()
                .age(25)
                .gender("male")
                .login("testUser25")
                .password("Pass123")
                .role("user")
                .screenName("TestScreen25")
                .build();

        Response response = playerClient.createPlayer("supervisor", request);
        response.then().log().all();

        CreatePlayerResponse created = response.as(CreatePlayerResponse.class);
        createdPlayerId = created.getId();

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(created.getId());
        Assert.assertEquals(created.getLogin(), request.getLogin());
    }

    @AfterMethod
    public void cleanup() {
        if (createdPlayerId != null) {
            playerClient.deletePlayer("supervisor", new DeletePlayerRequest(createdPlayerId));
            createdPlayerId = null;
        }
    }
}
