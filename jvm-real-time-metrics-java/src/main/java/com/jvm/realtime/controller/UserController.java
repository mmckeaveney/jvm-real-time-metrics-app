package com.jvm.realtime.controller;

import com.jvm.realtime.data.UserProcessor;
import com.jvm.realtime.model.ExceptionModel;
import com.jvm.realtime.model.SettingsModel;
import com.jvm.realtime.model.UserModel;
import com.jvm.realtime.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class UserController {

    private UserProcessor userProcessor;
    private UserRepository userRepository;

    @Autowired
    public UserController(UserProcessor userProcessor, UserRepository userRepository) {
        this.userProcessor = userProcessor;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/users/all", method = RequestMethod.GET)
    public List<String> getAllUsers() {
        return userRepository.findAll()
                                .stream()
                                .map(user -> user.getUsername())
                                .collect(Collectors.toList());
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
