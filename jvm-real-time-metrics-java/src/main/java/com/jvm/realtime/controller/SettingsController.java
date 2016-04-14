package com.jvm.realtime.controller;

import com.jvm.realtime.model.SettingsModel;
import com.jvm.realtime.persistence.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api")
public class SettingsController {

    private SettingsRepository settingsRepository;

    @Autowired
    public SettingsController(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public SettingsModel getSettings() {
        return settingsRepository.findOne("defaultSettings");
    }


    @RequestMapping(value = "/settings/save", method = RequestMethod.POST)
    public SettingsModel changeUserSettings(@RequestBody SettingsModel settings) {
        SettingsModel newSettings = new SettingsModel(settings.getDockerHost(), settings.getDockerPort());
        return settingsRepository.save(newSettings);
    }
}
