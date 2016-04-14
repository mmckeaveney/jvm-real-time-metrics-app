package com.jvm.realtime.data;

import com.jvm.realtime.config.ConfigurationProps;
import com.jvm.realtime.model.SettingsModel;
import com.jvm.realtime.model.UserModel;
import com.jvm.realtime.persistence.SettingsRepository;
import com.jvm.realtime.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserProcessor {

    private UserRepository userRepository;
    private ConfigurationProps configurationProps;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProcessor.class);

    @Autowired
    public UserProcessor(UserRepository userRepository, ConfigurationProps configurationProps) {
        this.userRepository = userRepository;
        this.configurationProps = configurationProps;
    }

    public UserModel provisionUser(String userId, String userName, String email) {
        if (!userRepository.exists(userId)) {
            LOGGER.info("User " + userName + " doesn't exist, provisioning...");

            UserModel userModel = new UserModel(
                    userId,
                    userName,
                    email,
                    Collections.emptySet()
            );

            userRepository.save(userModel);
            LOGGER.info("User " + userName + " provisioned with default settings.");
        }
        return new UserModel();
    }
}
