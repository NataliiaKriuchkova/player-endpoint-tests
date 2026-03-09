package com.player.client;

import com.player.core.HttpClient;
import com.player.model.request.CreatePlayerRequest;
import com.player.model.request.DeletePlayerRequest;
import com.player.model.request.GetPlayerRequest;
import com.player.model.request.UpdatePlayerRequest;
import com.player.utils.QueryParamsBuilder;
import io.restassured.response.Response;

import java.util.Map;
import java.util.Objects;

public class PlayerClient {

    private final HttpClient http;

    public PlayerClient(HttpClient http) {
        this.http = Objects.requireNonNull(http, "http client must not be null");
    }

    public Response createPlayer(String editor, CreatePlayerRequest request) {
        require(editor, "editor");
        require(request, "create player request");

        Map<String, Object> queryParams = new QueryParamsBuilder()
                .add("age", request.getAge())
                .add("gender", request.getGender())
                .add("login", request.getLogin())
                .add("password", request.getPassword())
                .add("role", request.getRole())
                .add("screenName", request.getScreenName())
                .build();

        return http.get(
                PlayerEndpoints.CREATE,
                Map.of("editor", editor),
                queryParams
        );
    }

    public Response getPlayer(GetPlayerRequest request) {
        require(request, "get player request");
        return http.post(PlayerEndpoints.GET, request);
    }

    public Response getAllPlayers() {
        return http.get(PlayerEndpoints.GET_ALL);
    }

    public Response deletePlayer(String editor, DeletePlayerRequest request) {
        require(editor, "editor");
        require(request, "delete player request");

        return http.delete(
                PlayerEndpoints.DELETE,
                Map.of("editor", editor),
                request
        );
    }

    public Response updatePlayer(String editor, Long id, UpdatePlayerRequest request) {
        require(editor, "editor");
        require(id, "player id");
        require(request, "update player request");

        return http.patch(
                PlayerEndpoints.UPDATE,
                Map.of("editor", editor, "id", id),
                request
        );
    }

    private static <T> T require(T value, String name) {
        return Objects.requireNonNull(value, name + " must not be null");
    }
}
