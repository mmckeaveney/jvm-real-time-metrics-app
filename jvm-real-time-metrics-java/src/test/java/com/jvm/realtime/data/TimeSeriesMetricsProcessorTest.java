package com.jvm.realtime.data;

import com.google.common.collect.Lists;
import com.jvm.realtime.model.ClientAppSnapshot;
import com.jvm.realtime.persistence.ClientAppSnapshotRepository;
import com.jvm.realtime.utils.TestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TimeSeriesMetricsProcessorTest {

    @Mock
    private MetricsEndpointProcessor metricsEndpointPoller;
    @Mock
    private ClientAppSnapshotRepository clientAppSnapshotRepository;
    @InjectMocks
    private TimeSeriesMetricsProcessor metricsProcessor;
    private TestData testData;

    @Before
    public void setUp() throws Exception {
        // Given
        this.testData = new TestData();
    }

    @Test
    public void testSaveDailyMetricsSnapshotWithSnapshotsAvailable() throws Exception {
        // When
        ClientAppSnapshot snapshotToSave = testData.clientAppSnapshot();
        Set<ClientAppSnapshot> snapshotList = new HashSet<>();
        snapshotList.add(snapshotToSave);
        metricsProcessor.saveDailyMetricsSnapshot(snapshotList);

        // Then
        verify(clientAppSnapshotRepository, atLeastOnce()).save(snapshotList);
    }

    @Test
    public void testSaveDailyMetricsSnapshotWithoutSnapshotsAvailable() throws Exception {
        // When
        metricsProcessor.saveDailyMetricsSnapshot(Collections.emptySet());

        // Then
        verify(clientAppSnapshotRepository, never()).save(anyCollection());
    }
}