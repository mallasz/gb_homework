package com.api.framework.stepdefinitions;

import com.api.framework.context.ScenarioContext;
import com.api.framework.utils.ApiClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for the {@code /posts} endpoint feature.
 *
 * <p>PicoContainer injects the same {@link ScenarioContext} instance used by
 * {@link Hooks}, so response data flows naturally between steps.
 */
public class PostsStepDefinitions {

    private final ScenarioContext context;

    public PostsStepDefinitions(ScenarioContext context) {
        this.context = context;
    }

    // ── Given ─────────────────────────────────────────────────────────────────

    /**
     * Override the default base URL for a scenario (useful for environment switching).
     */
    @Given("the API is available at {string}")
    public void theApiIsAvailableAt(String baseUrl) {
        context.setBaseUrl(baseUrl);
    }

    /**
     * Stores the Gherkin DataTable (key | value) directly as the request body.
     * REST-Assured serialises {@link Map} to JSON automatically, so this step
     * works for any resource type without any Java changes.
     */
    @Given("I have the following payload:")
    public void iHaveTheFollowingPayload(Map<String, String> payload) {
        context.setRequestBody(payload);
    }

    // ── When ──────────────────────────────────────────────────────────────────

    @When("I send a GET request to {string}")
    public void iSendAGetRequestTo(String endpoint) {
        Response response = ApiClient.given(context.getBaseUrl())
                .when()
                    .get(endpoint)
                .then()
                    .log().all()
                    .extract().response();
        context.setResponse(response);
    }

    @When("I send a POST request to {string}")
    public void iSendAPostRequestTo(String endpoint) {
        Response response = ApiClient.given(context.getBaseUrl())
                .body(context.getRequestBody())
                .when()
                    .post(endpoint)
                .then()
                    .log().all()
                    .extract().response();
        context.setResponse(response);
    }

    @When("I send a PUT request to {string}")
    public void iSendAPutRequestTo(String endpoint) {
        Response response = ApiClient.given(context.getBaseUrl())
                .body(context.getRequestBody())
                .when()
                    .put(endpoint)
                .then()
                    .log().all()
                    .extract().response();
        context.setResponse(response);
    }

    @When("I send a PATCH request to {string}")
    public void iSendAPatchRequestTo(String endpoint) {
        Response response = ApiClient.given(context.getBaseUrl())
                .body(context.getRequestBody())
                .when()
                    .patch(endpoint)
                .then()
                    .log().all()
                    .extract().response();
        context.setResponse(response);
    }

    @When("I send a DELETE request to {string}")
    public void iSendADeleteRequestTo(String endpoint) {
        Response response = ApiClient.given(context.getBaseUrl())
                .when()
                    .delete(endpoint)
                .then()
                    .log().all()
                    .extract().response();
        context.setResponse(response);
    }

    // ── Then ──────────────────────────────────────────────────────────────────

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expected) {
        assertThat(context.getResponse().getStatusCode())
                .as("HTTP status code")
                .isEqualTo(expected);
    }

    @Then("the response content type should contain {string}")
    public void theResponseContentTypeShouldContain(String expected) {
        assertThat(context.getResponse().getContentType())
                .as("Content-Type header")
                .containsIgnoringCase(expected);
    }

    @Then("the response should be a non-empty list")
    public void theResponseShouldBeANonEmptyList() {
        List<Object> list = context.getResponse().jsonPath().getList("$");
        assertThat(list)
                .as("Response body should be a non-empty JSON array")
                .isNotNull()
                .isNotEmpty();
    }

    /**
     * Verifies that every object in the response array contains all specified fields.
     *
     * @param commaSeparatedFields e.g. {@code "id,title,body,userId"}
     */
    @Then("each item in the list should have the fields {string}")
    public void eachItemInTheListShouldHaveTheFields(String commaSeparatedFields) {
        String[] fields = commaSeparatedFields.split(",");
        List<Map<String, Object>> items = context.getResponse().jsonPath().getList("$");

        assertThat(items).as("Response list should not be empty").isNotEmpty();

        for (Map<String, Object> item : items) {
            for (String field : fields) {
                assertThat(item)
                        .as("Item should contain field '%s'", field.trim())
                        .containsKey(field.trim());
            }
        }
    }

    @Then("the response field {string} should equal the integer {int}")
    public void theResponseFieldShouldEqualInteger(String field, int expected) {
        int actual = context.getResponse().jsonPath().getInt(field);
        assertThat(actual)
                .as("Response field '%s'", field)
                .isEqualTo(expected);
    }

    @Then("the response field {string} should equal {string}")
    public void theResponseFieldShouldEqualString(String field, String expected) {
        String actual = context.getResponse().jsonPath().getString(field);
        assertThat(actual)
                .as("Response field '%s'", field)
                .isEqualTo(expected);
    }

    @Then("the response field {string} should not be empty")
    public void theResponseFieldShouldNotBeEmpty(String field) {
        Object value = context.getResponse().jsonPath().get(field);
        assertThat(value)
                .as("Response field '%s' should not be null", field)
                .isNotNull();
        if (value instanceof String) {
            assertThat((String) value)
                    .as("Response field '%s' should not be blank", field)
                    .isNotBlank();
        }
    }

    @Then("the response field {string} should be a positive integer")
    public void theResponseFieldShouldBeAPositiveInteger(String field) {
        int value = context.getResponse().jsonPath().getInt(field);
        assertThat(value)
                .as("Response field '%s' should be a positive integer", field)
                .isPositive();
    }
}
