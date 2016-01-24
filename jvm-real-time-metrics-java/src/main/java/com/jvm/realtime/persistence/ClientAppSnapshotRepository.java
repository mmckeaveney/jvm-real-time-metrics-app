package com.jvm.realtime.persistence;

import com.jvm.realtime.model.ClientAppSnapshot;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface ClientAppSnapshotRepository extends MongoRepository<ClientAppSnapshot, String> {

    List<ClientAppSnapshot> findByTimeStamp(Long timeStamp, Sort sort);
    List<ClientAppSnapshot> findTop7ByAppName(String appName, Sort sort);
    List<ClientAppSnapshot> findTop31ByAppName(String appName, Sort sort);
    List<ClientAppSnapshot> findTop186ByAppName(String appName, Sort sort);
}
