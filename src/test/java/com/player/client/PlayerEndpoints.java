package com.player.client;

public final class PlayerEndpoints {
    public static final String CREATE = "/player/create/{editor}";
    public static final String DELETE = "/player/delete/{editor}";
    public static final String GET = "/player/get";
    public static final String UPDATE = "/player/update/{editor}/{id}";
    public static final String GET_ALL = "/player/get/all";

    private PlayerEndpoints() {
    }
}
