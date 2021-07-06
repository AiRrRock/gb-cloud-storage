package ru.aborichev.cloudstorage.core.service.environment.impl;

import java.util.HashMap;
import java.util.Map;

public class BasicConfigurationEnvironment implements ru.aborichev.cloudstorage.core.service.environment.ConfigurationEnvironment {
    private Map<String, Object> configurations;
    private static BasicConfigurationEnvironment instance;

    private BasicConfigurationEnvironment() {
        configurations = new HashMap<>();
    }

    public static BasicConfigurationEnvironment getInstance() {
        if (instance == null) {
            instance = new BasicConfigurationEnvironment();
        }
        return instance;
    }

    @Override
    public Object getProperty(String key) {
        return configurations.get(key);
    }

    @Override
    public void setProperty(String key, Object value) {
        configurations.put(key, value);
    }
}
