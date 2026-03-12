package com.api.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads test configuration from {@code config.properties} on the classpath.
 * System properties always take precedence, enabling CI overrides:
 * <pre>mvn test -Dbase.url=https://staging.example.com</pre>
 */
public final class ConfigManager {

    private static final Properties PROPERTIES = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        try (InputStream in = ConfigManager.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                throw new RuntimeException("Config file not found on classpath: " + CONFIG_FILE);
            }
            PROPERTIES.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + CONFIG_FILE, e);
        }
    }

    private ConfigManager() {}

    /**
     * Returns the value for {@code key}, throwing if it is absent.
     */
    public static String get(String key) {
        String value = System.getProperty(key, PROPERTIES.getProperty(key));
        if (value == null) {
            throw new RuntimeException("Missing required configuration key: " + key);
        }
        return value;
    }

    /**
     * Returns the value for {@code key}, falling back to {@code defaultValue}.
     */
    public static String get(String key, String defaultValue) {
        return System.getProperty(key, PROPERTIES.getProperty(key, defaultValue));
    }

    public static String getBaseUrl() {
        return get("base.url", "https://jsonplaceholder.org");
    }

    public static int getConnectTimeout() {
        String val = get("connect.timeout", "10000");
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid value for connect.timeout: '" + val + "'", e);
        }
    }

    public static int getReadTimeout() {
        String val = get("read.timeout", "10000");
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid value for read.timeout: '" + val + "'", e);
        }
    }
}
