package com.player.core;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class HttpClient {

    private final RequestSpecification spec;

    public HttpClient(RequestSpecification spec) {
        this.spec = spec;
    }

    private RequestSpecification request() {
        return given().spec(spec);
    }

    public Response get(String endpoint) {
        return request()
                .get(endpoint);
    }

    public Response get(String endpoint, Map<String, ?> pathParams, Map<String, ?> queryParams) {
        return request()
                .pathParams(pathParams)
                .queryParams(queryParams)
                .get(endpoint);
    }

    public Response post(String endpoint, Object body) {
        return request()
                .body(body)
                .post(endpoint);
    }

    public Response delete(String endpoint, Map<String, ?> pathParams, Object body) {
        return request()
                .pathParams(pathParams)
                .body(body)
                .delete(endpoint);
    }

    public Response patch(String endpoint, Map<String, ?> pathParams, Object body) {
        return request()
                .pathParams(pathParams)
                .body(body)
                .patch(endpoint);
    }
}
