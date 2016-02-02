package com.jvm.realtime.persistence;

import com.github.dockerjava.api.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<Event, String>{

    List<Event> findByFrom(String from);
}
