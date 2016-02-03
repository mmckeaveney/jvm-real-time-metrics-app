package com.jvm.realtime.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import org.springframework.data.annotation.Id;

public class DockerEvent {

    @Id
    private String id;
    @JsonProperty("status")
    private String status;
    @JsonProperty("from")
    private String image;
    @JsonProperty("time")
    private Long time;
    @JsonProperty("timeNano")
    private Long timeNano;

    public DockerEvent() {
    }

    public DockerEvent(String status, String id, String image, Long time, Long timeNano) {
        this.status = status;
        this.id = id;
        this.image = image;
        this.time = time;
        this.timeNano = timeNano;
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

    public Long getTimeNano() {
        return timeNano;
    }

    public void setTimeNano(Long timeNano) {
        this.timeNano = timeNano;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DockerEvent that = (DockerEvent) o;
        return Objects.equal(status, that.status) &&
                Objects.equal(id, that.id) &&
                Objects.equal(image, that.image) &&
                Objects.equal(time, that.time) &&
                Objects.equal(timeNano, that.timeNano);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(status, id, image, time, timeNano);
    }

    @Override
    public String toString() {
        return "DockerEvent{" +
                "status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", image='" + image + '\'' +
                ", time=" + time +
                ", timeNano=" + timeNano +
                '}';
    }
}
