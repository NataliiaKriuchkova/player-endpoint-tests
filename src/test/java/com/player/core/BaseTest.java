package com.player.core;

import com.player.client.PlayerClient;
import com.player.config.Config;
import com.player.mapper.PlayerMapper;
import com.player.model.Player;
import com.player.model.Role;
import com.player.service.PlayerService;
import com.player.utils.TestDataGenerator;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);

    protected final RequestSpecification spec = RestAssuredConfig.getRequestSpec();

    protected final HttpClient httpClient = new HttpClient(spec);

    protected final PlayerClient playerClient = new PlayerClient(httpClient);

    protected final PlayerService playerService = new PlayerService(playerClient);

    protected final String supervisor = Config.getSupervisorLogin();

    private final ThreadLocal<List<Long>> createdPlayers =
            ThreadLocal.withInitial(ArrayList::new);

    protected void registerPlayerForCleanup(Long id) {
        if (id != null) {
            createdPlayers.get().add(id);
        }
    }

    protected Player createUser() {
        return createPlayer(supervisor, TestDataGenerator.validPlayer());
    }

    protected Player createAdmin() {
        Player admin = TestDataGenerator.validPlayerBuilder()
                .role(Role.ADMIN.value())
                .build();

        return createPlayer(supervisor, admin);
    }

    protected Player createPlayer(String editor, Player player) {
        Player created = playerService.createPlayer(editor, player);
        registerPlayerForCleanup(created.getId());
        return created;
    }

    protected Player createPlayer(Player player) {
        Player created = playerService.createPlayer(supervisor, player);
        registerPlayerForCleanup(created.getId());
        return created;
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        log.info("Starting cleanup of created players");
        createdPlayers.get().forEach(this::safeDeletePlayer);
        createdPlayers.remove();
    }

    private void safeDeletePlayer(Long id) {
        try {
            log.info("Cleaning up player with id={}", id);
            playerClient.deletePlayer(supervisor, PlayerMapper.toDeleteRequest(id));
        } catch (Exception e) {
            log.warn("Failed to delete player during cleanup. id={}", id, e);
        }
    }
}
