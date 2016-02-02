package com.jvm.realtime.controller;

import com.github.dockerjava.api.model.Event;
import com.jvm.realtime.persistence.ClientAppSnapshotRepository;
import com.jvm.realtime.persistence.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @RequestMapping(value = "/events/all", method = RequestMethod.GET)
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public List<Event> getEventsForApp(@RequestParam(value = "appName") String appName) {
       return eventRepository.findByFrom(appName);
    }
}
