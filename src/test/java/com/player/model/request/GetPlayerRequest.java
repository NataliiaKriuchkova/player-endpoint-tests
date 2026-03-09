package com.player.model.request;

public class GetPlayerRequest {

    private final Long playerId;

    public GetPlayerRequest(Long playerId) {
        this.playerId = playerId;
    }

    public Long getPlayerId() { return playerId; }
}
