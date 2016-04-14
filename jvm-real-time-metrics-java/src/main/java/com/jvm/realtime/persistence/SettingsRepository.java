package com.jvm.realtime.persistence;

import com.jvm.realtime.model.SettingsModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SettingsRepository extends MongoRepository<SettingsModel, String> {
}
