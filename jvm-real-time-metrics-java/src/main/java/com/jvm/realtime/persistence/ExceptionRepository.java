package com.jvm.realtime.persistence;

import com.jvm.realtime.model.ExceptionModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExceptionRepository extends MongoRepository<ExceptionModel, String> {

}
