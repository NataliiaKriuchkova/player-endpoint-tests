package com.player.tests;

import com.player.client.PlayerClient;
import com.player.model.request.CreatePlayerRequest;
import com.player.model.request.UpdatePlayerRequest;
import com.player.model.response.CreatePlayerResponse;
import com.player.model.response.UpdatePlayerResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UpdatePlayerTest {
    private final PlayerClient playerClient = new PlayerClient();

    @Test
    public void shouldUpdatePlayer() {
        CreatePlayerRequest createRequest = CreatePlayerRequest.builder()
                .age(25)
                .gender("male")
                .login("testUpdate25")
                .password("Pass123")
                .role("user")
                .screenName("UpdateScreen25")
                .build();

        Response createResponse = playerClient.createPlayer("supervisor", createRequest);
        CreatePlayerResponse created = createResponse.as(CreatePlayerResponse.class);
        Assert.assertEquals(createResponse.statusCode(), 200);
        Assert.assertNotNull(created.getId());

        UpdatePlayerRequest updateRequest = UpdatePlayerRequest.builder()
                .age(30)
                .screenName("UpdatedScreen30")
                .build();

        Response updateResponse = playerClient.updatePlayer("supervisor", created.getId(), updateRequest);
        updateResponse.then().log().all();
        Assert.assertEquals(updateResponse.statusCode(), 200);

        UpdatePlayerResponse updated = updateResponse.as(UpdatePlayerResponse.class);
        Assert.assertEquals(updated.getAge(), Integer.valueOf(30));
        Assert.assertEquals(updated.getScreenName(), "UpdatedScreen30");
    }
}
