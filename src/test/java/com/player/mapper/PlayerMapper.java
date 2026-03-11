package com.player.mapper;

import com.player.model.Player;
import com.player.model.request.CreatePlayerRequest;
import com.player.model.request.DeletePlayerRequest;
import com.player.model.request.GetPlayerRequest;
import com.player.model.request.UpdatePlayerRequest;

public final class PlayerMapper {

    private PlayerMapper() {
    }

    public static CreatePlayerRequest toCreateRequest(Player player) {
        return CreatePlayerRequest.builder()
                .age(player.getAge())
                .gender(player.getGender())
                .login(player.getLogin())
                .password(player.getPassword())
                .role(player.getRole())
                .screenName(player.getScreenName())
                .build();
    }

    public static UpdatePlayerRequest toUpdateRequest(Player player) {
        return UpdatePlayerRequest.builder()
                .age(player.getAge())
                .gender(player.getGender())
                .login(player.getLogin())
                .password(player.getPassword())
                .screenName(player.getScreenName())
                .build();
    }

    public static DeletePlayerRequest toDeleteRequest(Long playerId) {
        return new DeletePlayerRequest(playerId);
    }

    public static GetPlayerRequest toGetRequest(Long playerId) {
        return new GetPlayerRequest(playerId);
    }
}
