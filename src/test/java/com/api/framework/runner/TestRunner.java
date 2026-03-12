package com.api.framework.runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.ANSI_COLORS_DISABLED_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

/**
 * Entry point for running the full Cucumber test suite via JUnit Platform.
 *
 * <p>Run all scenarios:
 * <pre>mvn test</pre>
 *
 * <p>Run a specific tag subset:
 * <pre>mvn test -Dcucumber.filter.tags="@smoke"</pre>
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME,      value = "com.api.framework.stepdefinitions")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,    value = "pretty," +
        "html:target/cucumber-reports/cucumber.html," +
        "json:target/cucumber-reports/cucumber.json," +
        "junit:target/cucumber-reports/cucumber.xml")
@ConfigurationParameter(key = ANSI_COLORS_DISABLED_PROPERTY_NAME, value = "true")
public class TestRunner {
    // Intentionally empty — JUnit Platform + Cucumber do the work.
}
