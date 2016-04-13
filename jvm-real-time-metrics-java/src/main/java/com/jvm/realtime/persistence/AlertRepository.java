package com.jvm.realtime.persistence;

import com.jvm.realtime.model.AlertModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AlertRepository extends MongoRepository<AlertModel, String> {

    AlertModel findById(String id);

    List<AlertModel> findByTriggeredIsFalse();

    List<AlertModel> findByTriggeredIsTrue();

    List<AlertModel> findByAppNameAndTriggeredIsTrue(String appName);

    AlertModel findTopByTriggeredIsTrueOrderByTimeLastTriggeredDesc();
}
