package com.jvm.realtime.data;

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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JvmRealTimeMetricsJavaApplication.class, EmbeddedMongoAutoConfiguration.class })
@TestPropertySource(locations="classpath:test.properties")
public class UserProcessorIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProcessor userProcessor;

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    public void testProvisionUser() {
        // When
        UserModel userModel = userProcessor.provisionUser("userId", "username", "email@test.com");

        // Then
        assertThat(userRepository.findOne("userId").getUserId(), is(userModel.getUserId()));
        assertThat(userRepository.findOne("userId").getUsername(), is(userModel.getUsername()));
        assertThat(userRepository.findOne("userId").getEmail(), is(userModel.getEmail()));
    }

    @Test
    public void testProvisionUserWhenUserExists() {
        // When
        userProcessor.provisionUser("userId", "username", "email@test.com");

        // Then
        // Ensure that another user has not been created.
        assertThat(userRepository.findAll().size(), is(1));
    }
}
