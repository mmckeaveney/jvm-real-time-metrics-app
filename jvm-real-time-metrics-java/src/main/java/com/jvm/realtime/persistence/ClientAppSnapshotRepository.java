package com.jvm.realtime.persistence;

import com.jvm.realtime.model.ClientAppSnapshot;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface ClientAppSnapshotRepository extends MongoRepository<ClientAppSnapshot, String> {

    List<ClientAppSnapshot> findTop7ByAppNameOrderByTimeStampDesc(String appName);
    List<ClientAppSnapshot> findTop31ByAppNameOrderByTimeStampDesc(String appName);
    List<ClientAppSnapshot> findTop186ByAppNameOrderByTimeStampDesc(String appName);
}
