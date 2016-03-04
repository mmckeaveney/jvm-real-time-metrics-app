package com.jvm.realtime.controller;

import com.jvm.realtime.data.UserProcessor;
import com.jvm.realtime.model.ExceptionModel;
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
}
