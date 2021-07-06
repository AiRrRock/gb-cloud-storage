package ru.aborichev.cloudstorage.core.service.environment;

import java.util.Map;

public interface ConfigurationEnvironment {
    Object getProperty(String key);

    void setProperty(String key, Object value);
}
