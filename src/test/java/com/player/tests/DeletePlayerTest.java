package com.player.tests;

import com.player.client.PlayerClient;
import com.player.model.request.CreatePlayerRequest;
import com.player.model.request.DeletePlayerRequest;
import com.player.model.request.GetPlayerRequest;
import com.player.model.response.CreatePlayerResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeletePlayerTest {
    private final PlayerClient playerClient = new PlayerClient();

    // BUG: returns 200 with empty body instead of 404 for deleted player
    @Test
    public void shouldDeletePlayer() {
        CreatePlayerRequest createRequest = CreatePlayerRequest.builder()
                .age(25)
                .gender("male")
                .login("testDelete25")
                .password("Pass123")
                .role("user")
                .screenName("DeleteScreen25")
                .build();

        Response createResponse = playerClient.createPlayer("supervisor", createRequest);
        CreatePlayerResponse created = createResponse.as(CreatePlayerResponse.class);
        Assert.assertEquals(createResponse.statusCode(), 200);
        Assert.assertNotNull(created.getId());

        Response deleteResponse = playerClient.deletePlayer("supervisor", new DeletePlayerRequest(created.getId()));
        deleteResponse.then().log().all();
        Assert.assertEquals(deleteResponse.statusCode(), 204);

        Response getResponse = playerClient.getPlayer(new GetPlayerRequest(created.getId()));
        getResponse.then().log().all();
        Assert.assertEquals(getResponse.statusCode(), 404);
    }
}
