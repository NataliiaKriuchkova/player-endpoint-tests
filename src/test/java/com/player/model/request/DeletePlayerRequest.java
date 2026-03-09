package com.player.model.request;

public class DeletePlayerRequest {

    private final Long playerId;

    public DeletePlayerRequest(Long playerId) {
        this.playerId = playerId;
    }

    public Long getPlayerId() { return playerId; }
}
