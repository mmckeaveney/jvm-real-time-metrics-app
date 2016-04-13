package com.jvm.realtime.persistence;

import com.jvm.realtime.model.QueryTimeModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QueryTimeRepository extends MongoRepository<QueryTimeModel, String> {
}
