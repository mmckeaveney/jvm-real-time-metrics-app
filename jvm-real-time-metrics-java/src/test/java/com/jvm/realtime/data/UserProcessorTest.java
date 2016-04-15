package com.jvm.realtime.data;

import com.jvm.realtime.config.ConfigurationProps;
import com.jvm.realtime.model.AlertModel;
import com.jvm.realtime.model.UserModel;
import com.jvm.realtime.persistence.UserRepository;
import com.jvm.realtime.utils.TestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserProcessorTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ConfigurationProps configurationProps;
    @InjectMocks
    private UserProcessor userProcessor;

    @Test
    public void testProvisionUser() {
        // When
        UserModel userModel = userProcessor.provisionUser("userId", "username", "email@test.com");

        // Then
        verify(userRepository, atLeastOnce()).save(any(UserModel.class));
        assertThat(userModel.getUserId(), is("userId"));
        assertThat(userModel.getUsername(), is("username"));
        assertThat(userModel.getEmail(), is("email@test.com"));
    }

}