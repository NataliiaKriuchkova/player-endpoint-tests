package com.player.core;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class HttpClient {

    private final RequestSpecification spec = RestAssuredConfig.getRequestSpec();

    public Response get(String endpoint) {
        return given()
                .spec(spec)
                .get(endpoint);
    }
}
