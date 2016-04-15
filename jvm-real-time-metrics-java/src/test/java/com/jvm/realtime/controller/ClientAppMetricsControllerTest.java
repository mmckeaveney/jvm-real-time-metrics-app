package com.jvm.realtime.controller;

import com.jvm.realtime.JvmRealTimeMetricsJavaApplication;
import com.jvm.realtime.model.ClientAppSnapshot;
import com.jvm.realtime.model.ClientAppTimeSeries;
import com.jvm.realtime.persistence.ClientAppSnapshotRepository;
import com.jvm.realtime.utils.TestData;
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
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JvmRealTimeMetricsJavaApplication.class, EmbeddedMongoAutoConfiguration.class })
@TestPropertySource(locations="classpath:test.properties")
public class ClientAppMetricsControllerTest {

    @Autowired
    private ClientAppMetricsController clientAppMetricsController;
    @Autowired
    private ClientAppSnapshotRepository clientAppSnapshotRepository;
    private TestData testData = new TestData();

    @Before
    public void setUp() throws Exception {
        clientAppSnapshotRepository.deleteAll();
    }

    @Test
    public void testGetAllClientApps() throws Exception {
        // Given
        ClientAppSnapshot snapshot = testData.clientAppSnapshot();
        clientAppSnapshotRepository.save(snapshot);

        // When
        List<String> names = clientAppMetricsController.getAllClientApps();

        // Then
        assertThat(names, contains(snapshot.getAppName()));
        assertThat(names.size(), is(1));
        assertThat(names.get(0), is(snapshot.getAppName()));
    }

    @Test
    public void testGetTimeSeriesDataForSingleAppInAWeek() throws Exception {
        // Given
        ClientAppSnapshot snapshot = testData.clientAppSnapshot();
        for (int i = 0; i < 7; i ++) {
            snapshot.setId(String.valueOf(i));
            clientAppSnapshotRepository.save(snapshot);
        }

        // When
        ClientAppTimeSeries timeSeries = clientAppMetricsController.getTimeSeriesDataForSingleApp("frankblizzard/jvmrtclientone", "week");

        // Then
        assertThat(timeSeries.getSnapshots().size(), is(7));
    }

    @Test
    public void testGetTimeSeriesDataForSingleAppInAMonth() throws Exception {
        // Given
        ClientAppSnapshot snapshot = testData.clientAppSnapshot();
        for (int i = 0; i < 186; i ++) {
            snapshot.setId(String.valueOf(i));
            clientAppSnapshotRepository.save(snapshot);
        }
        // When
        ClientAppTimeSeries timeSeries = clientAppMetricsController.getTimeSeriesDataForSingleApp("frankblizzard/jvmrtclientone", "month");

        // Then
        assertThat(timeSeries.getSnapshots().size(), is(31));
    }

    @Test
    public void testGetTimeSeriesDataForSingleAppInSixMonths() throws Exception {
        // Given
        ClientAppSnapshot snapshot = testData.clientAppSnapshot();
        for (int i = 0; i < 186; i ++) {
            snapshot.setId(String.valueOf(i));
            clientAppSnapshotRepository.save(snapshot);
        }
        // When
        ClientAppTimeSeries timeSeries = clientAppMetricsController.getTimeSeriesDataForSingleApp("frankblizzard/jvmrtclientone", "sixmonths");

        // Then
        assertThat(timeSeries.getSnapshots().size(), is(186));
    }
}