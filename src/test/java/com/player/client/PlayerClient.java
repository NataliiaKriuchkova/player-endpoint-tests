package com.player.client;

import com.player.core.HttpClient;
import io.restassured.response.Response;

public class PlayerClient {
    private final HttpClient http = new HttpClient();

    public Response getAllPlayers() {
        return http.get(PlayerEndpoints.GET_ALL);
    }
}
