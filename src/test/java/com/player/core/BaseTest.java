package com.player.core;

import com.player.client.PlayerClient;
import com.player.config.Config;
import com.player.model.request.DeletePlayerRequest;
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

    protected final String supervisor = Config.getSupervisorLogin();

    private final ThreadLocal<List<Long>> createdPlayers =
            ThreadLocal.withInitial(ArrayList::new);

    protected void registerPlayer(Long id) {
        if (id != null) {
            createdPlayers.get().add(id);
        }
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

            playerClient.deletePlayer(
                    supervisor,
                    new DeletePlayerRequest(id)
            );

        } catch (Exception e) {
            log.warn("Failed to delete player during cleanup. id={}", id, e);
        }
    }
}
