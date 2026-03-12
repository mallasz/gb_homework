package com.api.framework.stepdefinitions;

import com.api.framework.context.ScenarioContext;
import com.api.framework.utils.ConfigManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber lifecycle hooks.
 *
 * <p>{@link ScenarioContext} is injected by PicoContainer, ensuring the same
 * instance is shared with all other step-definition classes in the scenario.
 */
public class Hooks {

    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    private final ScenarioContext context;

    public Hooks(ScenarioContext context) {
        this.context = context;
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        log.info("========== START: {} ==========", scenario.getName());
        // Seed the base URL from config so individual scenarios can override it if needed.
        context.setBaseUrl(ConfigManager.getBaseUrl());
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed() && context.getResponse() != null) {
            log.warn("[FAILURE] Last response - Status: {}, Body:\n{}",
                    context.getResponse().getStatusCode(),
                    context.getResponse().getBody().asPrettyString());
        }
        log.info("========== END  : {} [{}] ==========", scenario.getName(), scenario.getStatus());
    }
}
