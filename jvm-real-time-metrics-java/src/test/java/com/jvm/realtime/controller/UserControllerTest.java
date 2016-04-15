package com.jvm.realtime.controller;

import com.jvm.realtime.JvmRealTimeMetricsJavaApplication;
import com.jvm.realtime.model.UserModel;
import com.jvm.realtime.persistence.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JvmRealTimeMetricsJavaApplication.class, EmbeddedMongoAutoConfiguration.class })
@TestPropertySource(locations="classpath:test.properties")
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserController userController;

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        // Given
        UserModel userModel = new UserModel("userId", "username", "email@email.com", Collections.emptySet());
        userRepository.save(userModel);

        // When
        List<String> usernames = userController.getAllUsers();

        // Then
        assertThat(usernames, contains(userModel.getUsername()));
        assertThat(usernames.size(), is(1));
    }

    @Test
    public void testCheckIfUserExistsAndBootstrap() throws Exception {
        // When
        userController.checkIfUserExistsAndBootstrap("userId", "username", "email@email.com");

        // Then
        assertThat(userRepository.findOne("userId").getUserId(), is("userId"));
        assertThat(userRepository.findOne("userId").getUsername(), is("username"));
        assertThat(userRepository.findOne("userId").getEmail(), is("email@email.com"));
        assertThat(userRepository.findAll().size(), is(1));
    }

    @Test
    public void testAddFavouriteForUser() throws Exception {
        // Given
        UserModel userModel = new UserModel("userId", "username", "email@email.com", Collections.emptySet());
        userRepository.save(userModel);

        // When
        userController.addFavouriteForUser("userId", "favouriteId");

        // Then
        assertThat(userRepository.findOne("userId").getFavourites(), contains("favouriteId"));
        assertThat(userRepository.findOne("userId").getFavourites().size(), is(1));
    }

    @Test
    public void testGetFavouritesForUser() throws Exception {
        // Given
        UserModel userModel = new UserModel("userId", "username", "email@email.com", Collections.emptySet());
        userRepository.save(userModel);
        userController.addFavouriteForUser("userId", "favouriteId");

        // When
        Set<String> favourites = userController.getFavouritesForUser("userId");

        // Then
        assertThat(favourites, contains("favouriteId"));
        assertThat(favourites.size(), is(1));
    }
}