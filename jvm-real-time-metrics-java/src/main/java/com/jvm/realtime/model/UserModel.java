package com.jvm.realtime.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
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
    @JsonProperty("favourites")
    private Set<String> favourites;

    public UserModel() {
    }

    public UserModel(String userId, String username, String email, SettingsModel userSettings, Set<AlertModel> alerts) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.userSettings = userSettings;
        this.alerts = alerts;
        this.favourites = new HashSet<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SettingsModel getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(SettingsModel userSettings) {
        this.userSettings = userSettings;
    }

    public Set<AlertModel> getAlerts() {
        return alerts;
    }

    public void setAlerts(Set<AlertModel> alerts) {
        this.alerts = alerts;
    }

    public Set<String> getFavourites() {
        return favourites;
    }

    public void setFavourites(Set<String> favourites) {
        this.favourites = favourites;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("userId", userId)
                .add("username", username)
                .add("email", email)
                .add("userSettings", userSettings)
                .add("alerts", alerts)
                .add("favourites", favourites)
                .toString();
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
                Objects.equal(alerts, userModel.alerts) &&
                Objects.equal(favourites, userModel.favourites);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId, username, email, userSettings, alerts, favourites);
    }

}
