package com.jvm.realtime.controller;

import com.jvm.realtime.JvmRealTimeMetricsJavaApplication;
import com.jvm.realtime.model.DockerEvent;
import com.jvm.realtime.persistence.EventRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JvmRealTimeMetricsJavaApplication.class, EmbeddedMongoAutoConfiguration.class })
@TestPropertySource(locations="classpath:test.properties")
public class EventControllerTest {

    @Autowired
    private EventController eventController;
    @Autowired
    private EventRepository eventRepository;

    @Before
    public void setUp() throws Exception {
        eventRepository.deleteAll();
    }

    @Test
    public void testGetAllEvents() throws Exception {
        // Given
        DockerEvent event = new DockerEvent("status", "image", 0L);
        eventRepository.save(event);

        // When
        List<DockerEvent> eventList = eventController.getAllEvents();

        // Then
        assertThat(eventList, contains(event));
        assertThat(eventList.size(), is(1));

    }

    @Test
    public void testGetEventsForApp() throws Exception {
        // Given
        DockerEvent event = new DockerEvent("status", "image", 0L);
        DockerEvent otherEvent = new DockerEvent("otherstatus", "otherimage", 0L);
        eventRepository.save(event);
        eventRepository.save(otherEvent);

        // When
        List<DockerEvent> eventsForApp = eventController.getEventsForApp("image");

        // Then
        assertThat(eventsForApp, contains(event));
        assertThat(eventsForApp, not(contains(otherEvent)));
        assertThat(eventsForApp.size(), is(1));
        assertThat(eventsForApp.get(0).getImage(), is("image"));
        assertThat(eventsForApp.get(0).getImage(), is(not("otherimage")));
    }

    @Test
    public void testGetMostRecentEvent() throws Exception {
        // Given
        DockerEvent event = new DockerEvent("status", "image", 0L);
        DockerEvent moreRecentEvent = new DockerEvent("otherstatus", "otherimage", 1L);
        eventRepository.save(event);
        eventRepository.save(moreRecentEvent);

        // When
        List<DockerEvent> mostRecent = eventController.getMostRecentEvent();

        // Then
        assertThat(mostRecent, contains(moreRecentEvent));
        assertThat(mostRecent, not(contains(event)));
        assertThat(mostRecent.size(), is(1));
        assertThat(mostRecent.get(0), is(moreRecentEvent));

    }
}