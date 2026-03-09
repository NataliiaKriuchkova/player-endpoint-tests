package com.player.core;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class HttpClient {

    private final RequestSpecification spec = RestAssuredConfig.getRequestSpec();

    public Response get(String endpoint) {
        return given()
                .spec(spec)
                .get(endpoint);
    }

    public Response get(String endpoint, Map<String, ?> pathParams, Map<String, ?> queryParams) {
        return given()
                .spec(spec)
                .pathParams(pathParams)
                .queryParams(queryParams)
                .get(endpoint);
    }

    public Response post(String endpoint, Object body) {
        return given()
                .spec(spec)
                .body(body)
                .post(endpoint);
    }

    public Response delete(String endpoint, String pathParam, Object body) {
        return given()
                .spec(spec)
                .pathParam("editor", pathParam)
                .body(body)
                .delete(endpoint);
    }

    public Response patch(String endpoint, Map<String, ?> pathParams, Object body) {
        return given()
                .spec(spec)
                .pathParams(pathParams)
                .body(body)
                .patch(endpoint);
    }
}
