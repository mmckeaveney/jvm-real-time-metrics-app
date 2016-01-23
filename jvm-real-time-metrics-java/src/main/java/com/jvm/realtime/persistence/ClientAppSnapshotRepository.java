package com.jvm.realtime.persistence;

import com.jvm.realtime.model.ClientAppSnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;

public interface ClientAppSnapshotRepository extends MongoRepository<ClientAppSnapshot, String> {

//    public Set<ClientAppSnapshot> findLastTenSnapshots();
    public ClientAppSnapshot findByAppName(String appName);
}
