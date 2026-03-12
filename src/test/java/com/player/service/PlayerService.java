package com.player.service;

import com.player.assertions.PlayerAssertions;
import com.player.client.PlayerClient;
import com.player.mapper.PlayerMapper;
import com.player.model.Player;
import com.player.model.request.UpdatePlayerRequest;
import com.player.model.response.CreatePlayerResponse;
import com.player.model.response.GetAllPlayersResponse;
import com.player.model.response.PlayerItem;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.mapper.ObjectMapperType.JACKSON_2;

public class PlayerService {

    private static final Logger log = LogManager.getLogger(PlayerService.class);

    private final PlayerClient client;

    public PlayerService(PlayerClient client) {
        this.client = client;
    }

    public Response attemptCreatePlayer(String editor, Player player) {
        return client.createPlayer(editor, PlayerMapper.toCreateRequest(player));
    }

    public Player createPlayer(String editor, Player player) {
        Response response = attemptCreatePlayer(editor, player);
        PlayerAssertions.assertSuccessResponse(response);
        CreatePlayerResponse created = response.as(CreatePlayerResponse.class, JACKSON_2);
        return player.withId(created.getId());
    }

    public Player getPlayer(long playerId) {
        Response response = client.getPlayer(PlayerMapper.toGetRequest(playerId));
        PlayerAssertions.assertSuccessResponse(response);

        return response.as(Player.class, JACKSON_2);
    }

    public List<PlayerItem> getAllPlayers() {
        Response response = client.getAllPlayers();
        PlayerAssertions.assertSuccessResponse(response);

        return response.as(GetAllPlayersResponse.class, JACKSON_2).getPlayers();
    }

    public long countPlayersByScreenName(String screenName) {
        log.info("Counting players with screenName='{}'", screenName);
        return getAllPlayers().stream()
                .map(PlayerItem::getScreenName)
                .filter(screenName::equals)
                .count();
    }

    public Long findPlayerIdByAgeAndScreenName(int age, String screenName) {
        return getAllPlayers().stream()
                .filter(p -> p.getAge() != null && p.getAge() == age)
                .filter(p -> screenName.equals(p.getScreenName()))
                .map(PlayerItem::getId)
                .findFirst()
                .orElse(null);
    }

    public Long findPlayerIdByScreenName(String screenName) {
        return getAllPlayers().stream()
                .filter(p -> screenName.equals(p.getScreenName()))
                .map(PlayerItem::getId)
                .findFirst()
                .orElse(null);
    }

    public List<Long> findPlayerIdsByScreenName(String screenName) {
        return getAllPlayers().stream()
                .filter(p -> screenName.equals(p.getScreenName()))
                .map(PlayerItem::getId)
                .collect(Collectors.toList());
    }

    public boolean playerExistsById(long playerId) {
        return getAllPlayers().stream()
                .anyMatch(p -> p.getId() != null && p.getId() == playerId);
    }

    public void deletePlayer(String editor, long playerId) {
        client.deletePlayer(editor, PlayerMapper.toDeleteRequest(playerId));
    }

    public Player updatePlayer(String editor, Player player) {
        Response response = client.updatePlayer(
                editor,
                player.getId(),
                PlayerMapper.toUpdateRequest(player)
        );
        PlayerAssertions.assertSuccessResponse(response);

        return response.as(Player.class, JACKSON_2);
    }

    public Player updatePlayer(String editor, long id, UpdatePlayerRequest request) {
        Response response = client.updatePlayer(editor, id, request);
        PlayerAssertions.assertSuccessResponse(response);
        return response.as(Player.class, JACKSON_2);
    }
}
