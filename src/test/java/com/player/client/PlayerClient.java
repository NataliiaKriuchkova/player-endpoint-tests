package com.player.client;

import com.player.core.HttpClient;
import com.player.model.request.CreatePlayerRequest;
import com.player.model.request.DeletePlayerRequest;
import com.player.model.request.GetPlayerRequest;
import com.player.model.request.UpdatePlayerRequest;
import io.restassured.response.Response;

import java.util.Map;

public class PlayerClient {
    private final HttpClient http = new HttpClient();

    public Response createPlayer(String editor, CreatePlayerRequest request) {
        return http.get(
                PlayerEndpoints.CREATE,
                Map.of("editor", editor),
                Map.of(
                        "age", request.getAge(),
                        "gender", request.getGender(),
                        "login", request.getLogin(),
                        "password", request.getPassword(),
                        "role", request.getRole(),
                        "screenName", request.getScreenName()
                )
        );
    }

    public Response getPlayer(GetPlayerRequest request) {
        return http.post(PlayerEndpoints.GET, request);
    }

    public Response getAllPlayers() {
        return http.get(PlayerEndpoints.GET_ALL);
    }

    public Response deletePlayer(String editor, DeletePlayerRequest request) {
        return http.delete(PlayerEndpoints.DELETE, editor, request);
    }

    public Response updatePlayer(String editor, Long id, UpdatePlayerRequest request) {
        return http.patch(
                PlayerEndpoints.UPDATE,
                Map.of("editor", editor, "id", id),
                request
        );
    }
}
