package com.jvm.realtime.data;

import com.jvm.realtime.JvmRealTimeMetricsJavaApplication;
import com.jvm.realtime.model.ClientAppSnapshot;
import com.jvm.realtime.model.ExceptionModel;
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
import java.util.HashSet;
import java.util.Set;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JvmRealTimeMetricsJavaApplication.class, EmbeddedMongoAutoConfiguration.class })
@TestPropertySource(locations="classpath:test.properties")
public class TimeSeriesMetricsProcessorIntegrationTest {

    @Autowired
    private TimeSeriesMetricsProcessor metricsProcessor;
    @Autowired
    private ClientAppSnapshotRepository clientAppSnapshotRepository;
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
        assertThat(clientAppSnapshotRepository.findOne("id"), is(snapshotToSave));
    }
}
