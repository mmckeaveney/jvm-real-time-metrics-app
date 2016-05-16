package com.jvm.realtime.controller;

import com.jvm.realtime.data.UserProcessor;
import com.jvm.realtime.model.ClientAppSnapshot;
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
    public UserModel checkIfUserExistsAndBootstrap(@RequestParam String id,
                                                   @RequestParam String uname,
                                                   @RequestParam String email) {
        userProcessor.provisionUser(id, uname, email);
        return new UserModel();
    }




    @RequestMapping(value = "/user/favourites/save", method = RequestMethod.POST)
    public void addFavouriteForUser(@RequestParam String userId,
                                    @RequestParam String favourite) {
       UserModel currentUser = userRepository.findByUserId(userId);
       if (!currentUser.getFavourites().contains(favourite)) {
           currentUser.getFavourites().add(favourite);
           userRepository.save(currentUser);
       }
    }

    @RequestMapping(value = "/user/favourites/find", method = RequestMethod.GET)
    public Set<String> getFavouritesForUser(@RequestParam String userId) {
        UserModel currentUser = userRepository.findByUserId(userId);
        return currentUser.getFavourites();
    }
}
