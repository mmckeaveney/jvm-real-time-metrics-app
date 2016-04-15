package com.jvm.realtime.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.Container;
import com.jvm.realtime.model.ClientAppSnapshot;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestData {

    private ClassLoader classLoader;
    private ObjectMapper mapper;

    public TestData() {
        this.classLoader = getClass().getClassLoader();
        this.mapper = new ObjectMapper();
    }

    public File getJson(String path) {
        return new File(classLoader.getResource(path).getFile());
    }

    public Container dockerContainer() throws IOException {
        Container dockerContainer = mapper.readValue(getJson("testjson/container.json"), Container.class);
        return dockerContainer;
    }

    public List<Container> dockerContainers() throws IOException {
        List<Container> dockerContainers = mapper.readValue(getJson("testjson/containers.json"), new TypeReference<List<Container>>(){});
        return dockerContainers;
    }

    public Map<String, Object> testMetrics() throws IOException {
       Map<String, Object> metrics = mapper.readValue(getJson("testjson/metrics.json"), new TypeReference<Map<String, Object>>(){});
       return metrics;
    }

    public ClientAppSnapshot clientAppSnapshot() throws IOException {
        return mapper.readValue(getJson("testjson/clientapp.json"), ClientAppSnapshot.class);
    }


}
