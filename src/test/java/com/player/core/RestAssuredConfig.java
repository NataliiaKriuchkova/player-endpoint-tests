package com.player.core;

import com.player.config.Config;
import io.restassured.RestAssured;

public class RestAssuredConfig {
    static {
        RestAssured.baseURI = Config.getAppBaseUrl();
    }
}
