package com.jvm.realtime.rest;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.api.model.Filters;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.EventsResultCallback;
import com.jvm.realtime.config.Config;
import com.jvm.realtime.model.DockerEvent;
import com.jvm.realtime.persistence.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class EventPoller implements DataPoller {

    private DockerClient dockerClient;
    private final EventRepository eventRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(EventPoller.class);

    @Autowired
    public EventPoller(EventRepository eventRepository) {
        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
                .withVersion("1.18")
                .withUri("http://" + Config.dockerHost + ":2376")
                .build();
        this.dockerClient = DockerClientBuilder.getInstance(config).build();
        this.eventRepository = eventRepository;
    }

    @Override
    public void poll() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                updateDockerEvents();
            }
        }, 0, 20000);
    }

    private void updateDockerEvents() {
        EventsCallback eventCallback = new EventsCallback();
        try {
            this.dockerClient.eventsCmd()
                    .withSince("1374067924")
                    .exec(eventCallback);

        } catch (Exception e) {
            LOGGER.error("Exception getting events from docker API", e);
        }
    }

    private class EventsCallback extends EventsResultCallback {
        public void onNext(Event event) {
            DockerEvent currentEvent = new DockerEvent();
            currentEvent.setStatus(event.getStatus());
            currentEvent.setImage(event.getFrom());
            currentEvent.setTime(event.getTime());
            eventRepository.save(currentEvent);
            LOGGER.info("Received event : {} from image {} at {}",
                    currentEvent.getStatus(),
                    currentEvent.getImage(),
                    System.currentTimeMillis());
        }
    }

}
