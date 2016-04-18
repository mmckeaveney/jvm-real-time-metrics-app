package com.jvm.realtime.config;

import com.jvm.realtime.model.SettingsModel;
import com.jvm.realtime.persistence.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// Dynamic configuration settings that update when the user saves new settings from the UI.
@Component
public class ConfigurationProps {

    private SettingsRepository settingsRepository;
    private String dockerHost;
    private int dockerPort;

    @Autowired
    public ConfigurationProps(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
        provisionSettings();
        SettingsModel currentSettings = settingsRepository.findOne("defaultSettings");
        this.dockerHost = currentSettings.getDockerHost();
        this.dockerPort = currentSettings.getDockerPort();
    }

    private void provisionSettings() {
        if (settingsRepository.findAll().isEmpty()) {
            settingsRepository.save(new SettingsModel("localhost", 2376));
        }
    }

    public SettingsRepository getSettingsRepository() {
        return settingsRepository;
    }

    public void setSettingsRepository(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public String getDockerHost() {
        return dockerHost;
    }

    public void setDockerHost(String dockerHost) {
        this.dockerHost = dockerHost;
    }

    public int getDockerPort() {
        return dockerPort;
    }

    public void setDockerPort(int dockerPort) {
        this.dockerPort = dockerPort;
    }
}
