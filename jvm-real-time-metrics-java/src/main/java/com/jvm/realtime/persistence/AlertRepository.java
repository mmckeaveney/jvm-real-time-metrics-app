package com.jvm.realtime.persistence;

import com.jvm.realtime.model.AlertModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlertRepository extends MongoRepository<AlertModel, String> {

}
