package com.jvm.realtime.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.data.annotation.Id;

import java.util.Set;

public class UserModel {

    @Id
    private String userId;
    @JsonProperty("userName")
    private String username;
    @JsonProperty("email")
    private String email;
    @JsonProperty("userSettings")
    private SettingsModel userSettings;
    @JsonProperty("alerts")
    private Set<AlertModel> alerts;

    public UserModel() {
    }

    public UserModel(String userId, String username, String email, SettingsModel userSettings, Set<AlertModel> alerts) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.userSettings = userSettings;
        this.alerts = alerts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return Objects.equal(userId, userModel.userId) &&
                Objects.equal(username, userModel.username) &&
                Objects.equal(email, userModel.email) &&
                Objects.equal(userSettings, userModel.userSettings) &&
                Objects.equal(alerts, userModel.alerts);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId, username, email, userSettings, alerts);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("userId", userId)
                .add("username", username)
                .add("email", email)
                .add("userSettings", userSettings)
                .add("alerts", alerts)
                .toString();
    }
}
