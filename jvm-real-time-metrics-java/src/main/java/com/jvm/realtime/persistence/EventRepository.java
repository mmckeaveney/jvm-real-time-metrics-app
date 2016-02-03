package com.jvm.realtime.persistence;

import com.jvm.realtime.model.DockerEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<DockerEvent, String>{

    List<DockerEvent> findByImage(String image);
}
