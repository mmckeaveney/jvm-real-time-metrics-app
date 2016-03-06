package com.jvm.realtime.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.data.annotation.Id;

public class DockerEvent {

    @Id
    private String id;
    @JsonProperty("status")
    private String status;
    @JsonProperty("image")
    private String image;
    @JsonProperty("time")
    private Long time;

    public DockerEvent() {
    }

    public DockerEvent(String status, String image, Long time) {
        this.id = image + status + time;
        this.status = status;
        this.image = image;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DockerEvent that = (DockerEvent) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(status, that.status) &&
                Objects.equal(image, that.image) &&
                Objects.equal(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, status, image, time);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("status", status)
                .add("image", image)
                .add("time", time)
                .toString();
    }
}
