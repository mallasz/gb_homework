package com.api.framework.context;

import io.restassured.response.Response;

/**
 * Holds all mutable state for a single Cucumber scenario.
 *
 * <p>An instance is created by PicoContainer at the start of each scenario and
 * injected into every step-definition class that declares it as a constructor
 * parameter, ensuring consistent shared state across step classes.
 */
public class ScenarioContext {

    /** The API base URL used for all requests in the current scenario. */
    private String baseUrl;

    /** The last HTTP response received. */
    private Response response;

    /**
     * Request body built by "Given" steps and consumed by "When" steps.
     * Typed as {@link Object} so any model class can be stored without
     * widening this context to accommodate each new resource type.
     */
    private Object requestBody;

    // ── Accessors ────────────────────────────────────────────────────────────

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Object getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
    }
}
