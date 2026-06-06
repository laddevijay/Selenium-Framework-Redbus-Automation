package com.redbus.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ConfigReader {

	/**
	 * This is a utility class and cannot be instantiated or extended (final class +
	 * private constructor).
	 *
	 * Configuration is loaded once at class initialization and shared across all
	 * threads. Since properties are read-only after loading, concurrent access is
	 * thread-safe.
	 */

	private static final Properties properties = new Properties();
	private static final Logger logger = LogManager.getLogger(ConfigReader.class);

	static {
		loadProperties();
	}

	private ConfigReader() {
	}

	private static void loadProperties() {

		// Step 1: Get env from JVM (highest priority)
		String env = System.getProperty("env");

		String fileName = "config/config-" + env + ".properties";

		try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {

			if (is == null) {
				throw new RuntimeException("Config file NOT found: " + fileName);
			}

			properties.load(is);
			logger.info("Loaded config for env: {}", env);

		} catch (Exception e) {
			logger.error("Failed to load config file: {}", fileName, e);
			throw new IllegalStateException(e);
		}
	}

	// ------------------- Getters -------------------

	public static String getEnv() {
		return getProperty("env");
	}

	public static String getBaseUrl() {
		return getProperty("base.url");
	}

	public static String getBrowser() {
		return getProperty("browser");
	}

	public static int getImplicitWait() {
		return Integer.parseInt(getProperty("implicit.wait"));
	}

	public static int getExplicitWait() {
		return Integer.parseInt(getProperty("explicit.wait"));
	}

	public static int getPageLoadTimeout() {
		return Integer.parseInt(getProperty("page.load.timeout"));
	}

	public static boolean isScreenshotEnabled() {
		return Boolean.parseBoolean(getProperty("screenshot.on.failure"));
	}

	public static int getRetryCount() {
		return Integer.parseInt(getProperty("retry.count"));
	}

	// ------------------- Common -------------------

	private static String getProperty(String key) {

		String value = properties.getProperty(key);

		if (value == null || value.trim().isEmpty()) {
			logger.error("Missing property: {}", key);
			throw new IllegalStateException("Property not found: " + key);
		}

		return value.trim();
	}
}