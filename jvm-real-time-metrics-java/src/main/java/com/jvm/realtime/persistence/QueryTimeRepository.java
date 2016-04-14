package com.jvm.realtime.persistence;

import com.jvm.realtime.model.QueryTimeModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QueryTimeRepository extends MongoRepository<QueryTimeModel, String> {

    List<QueryTimeModel> findByApplicationName(String appName);

    QueryTimeModel findTopByOrderByTimeExecuted();
}
