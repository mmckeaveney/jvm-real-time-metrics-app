package com.jvm.realtime.persistence;

import com.jvm.realtime.model.ExceptionModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExceptionRepository extends MongoRepository<ExceptionModel, String> {

    List<ExceptionModel> findByApplicationName(String appName);

    ExceptionModel findTopByOrderByTimeDesc();

}
