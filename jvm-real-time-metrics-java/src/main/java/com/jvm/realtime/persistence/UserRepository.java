package com.jvm.realtime.persistence;

import com.jvm.realtime.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserModel, String> {

    UserModel findByUserId(String userId);
}
