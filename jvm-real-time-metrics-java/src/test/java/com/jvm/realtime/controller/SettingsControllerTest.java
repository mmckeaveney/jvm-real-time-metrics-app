package com.jvm.realtime.controller;

import com.jvm.realtime.JvmRealTimeMetricsJavaApplication;
import com.jvm.realtime.model.SettingsModel;
import com.jvm.realtime.persistence.SettingsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JvmRealTimeMetricsJavaApplication.class, EmbeddedMongoAutoConfiguration.class })
@TestPropertySource(locations="classpath:test.properties")
public class SettingsControllerTest {

    @Autowired
    private SettingsController settingsController;
    @Autowired
    private SettingsRepository settingsRepository;

    @Before
    public void setUp() throws Exception {
        settingsRepository.deleteAll();
    }

    @Test
    public void testGetSettings() throws Exception {
        // Given
        SettingsModel settingsModel = new SettingsModel("localhost", 1234);
        settingsRepository.save(settingsModel);

        // When
        SettingsModel currentSettings = settingsController.getSettings();

        // Then
        assertThat(currentSettings.getDockerHost(), is("localhost"));
        assertThat(currentSettings.getDockerPort(), is(1234));

    }

    @Test
    public void testChangeUserSettings() throws Exception {
        // Given
        SettingsModel settingsModel = new SettingsModel("localhost", 1234);
        settingsRepository.save(settingsModel);
        assertThat(settingsRepository.findOne("defaultSettings").getDockerHost(), is("localhost"));
        assertThat(settingsRepository.findOne("defaultSettings").getDockerPort(), is(1234));

        // When
        settingsController.changeUserSettings(new SettingsModel("changedhost", 9000));

        // Then
        assertThat(settingsRepository.findOne("defaultSettings").getDockerHost(), is("changedhost"));
        assertThat(settingsRepository.findOne("defaultSettings").getDockerPort(), is(9000));
    }
}