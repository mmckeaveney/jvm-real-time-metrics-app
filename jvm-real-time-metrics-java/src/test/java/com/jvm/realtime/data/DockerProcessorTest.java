package com.jvm.realtime.data;

import com.jvm.realtime.config.ConfigurationProps;
import com.jvm.realtime.model.ClientAppSnapshot;
import com.jvm.realtime.utils.TestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.github.dockerjava.api.model.Container;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DockerProcessorTest {

    @InjectMocks
    private DockerProcessor dockerProcessor;
    @Mock
    SimpMessagingTemplate websocket;
    @Mock
    ConfigurationProps configurationProps;
    private TestData testData;

    @Before
    public void setUp() throws Exception {
       this.testData = new TestData();
    }

    @Test
    public void testGetDockerApplicationMetadata() throws IOException {
        // Given
        Container container = testData.dockerContainer();

        // When
        ClientAppSnapshot metadata = dockerProcessor.getDockerApplicationMetaData(container);

        // Then
        assertThat(metadata.getAppName(), is(container.getImage()));
        assertThat(metadata.getContainerId(), is(container.getId()));
        assertThat(metadata.getPublicPort(), is(container.getPorts()[0].getPublicPort()));
        assertThat(metadata.getAppName(), is(container.getImage()));
        assertThat(metadata.getAppName(), is(container.getImage()));
    }

}