package com.jvm.realtime.controller;

import com.jvm.realtime.data.UserProcessor;
import com.jvm.realtime.model.ExceptionModel;
import com.jvm.realtime.model.SettingsModel;
import com.jvm.realtime.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class UserController {

    private UserProcessor userProcessor;

    @Autowired
    public UserController(UserProcessor userProcessor) {
        this.userProcessor = userProcessor;
    }

    @RequestMapping(value = "/usercheck", method = RequestMethod.POST)
    public UserModel checkIfUserExistsAndBootstrap(@RequestParam String userId,
                                                   @RequestParam String userName,
                                                   @RequestParam String email) {
        userProcessor.provisionUser(userId, userName, email);
        return new UserModel();
    }


    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public SettingsModel checkIfUserExistsAndBootstrap(@RequestParam String userId) {
        return userProcessor.getSettingsForUser(userId);
    }


    @RequestMapping(value = "/settings/save", method = RequestMethod.POST)
    public SettingsModel changeUserSettings(@RequestParam String userId,
                                            @RequestBody SettingsModel settings) {
        return userProcessor.changeUserSettings(userId, settings);
    }

}
