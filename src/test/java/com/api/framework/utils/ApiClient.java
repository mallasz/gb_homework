package com.api.framework.utils;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Factory for pre-configured REST-assured {@link RequestSpecification} instances.
 *
 * <p>Call {@link #given(String)} to obtain a request spec that already has:
 * <ul>
 *   <li>Base URI set from the supplied argument</li>
 *   <li>{@code Content-Type: application/json} and {@code Accept: application/json}</li>
 *   <li>Connection and socket timeouts from {@link ConfigManager}</li>
 *   <li>Full request logging</li>
 * </ul>
 */
public final class ApiClient {

    private static final RestAssuredConfig CONFIG = RestAssured.config()
            .httpClient(HttpClientConfig.httpClientConfig()
                    .setParam("http.connection.timeout", ConfigManager.getConnectTimeout())
                    .setParam("http.socket.timeout", ConfigManager.getReadTimeout()));

    private ApiClient() {}

    public static RequestSpecification given(String baseUrl) {
        return RestAssured.given()
                .config(CONFIG)
                .baseUri(baseUrl)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .log().all();
    }
}
