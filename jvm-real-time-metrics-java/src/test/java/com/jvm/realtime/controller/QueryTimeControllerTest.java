package com.jvm.realtime.controller;

import com.jvm.realtime.JvmRealTimeMetricsJavaApplication;
import com.jvm.realtime.model.QueryTimeModel;
import com.jvm.realtime.persistence.QueryTimeRepository;
import org.hamcrest.Matchers;
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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JvmRealTimeMetricsJavaApplication.class, EmbeddedMongoAutoConfiguration.class })
@TestPropertySource(locations="classpath:test.properties")
public class QueryTimeControllerTest {

    @Autowired
    private QueryTimeController queryTimeController;
    @Autowired
    private QueryTimeRepository queryTimeRepository;

    @Before
    public void setUp() throws Exception {
        queryTimeRepository.deleteAll();
    }

    @Test
    public void testHandleQueryTimeFromClient() throws Exception {
        // Given
        QueryTimeModel queryTimeModel = new QueryTimeModel("appname", "classname", "methodname", 200L, 0L);
        String id = queryTimeModel.getApplicationName() + queryTimeModel.getClassName() + queryTimeModel.getMethodName() + queryTimeModel.getTimeExecuted();

        // When
        queryTimeController.handleQueryTimeFromClient(queryTimeModel);

        // Then
        assertThat(queryTimeRepository.findOne(id), is(queryTimeModel));
        assertThat(queryTimeRepository.findAll().size(), is(1));
    }

    @Test
    public void testGetMostRecentQueryTime() throws Exception {
        // Given
        QueryTimeModel queryTimeModel = new QueryTimeModel("appname", "classname", "methodname", 200L, 0L);
        QueryTimeModel moreRecentQueryTime = new QueryTimeModel("appname2", "classname", "methodname", 200L, 1000L);
        queryTimeRepository.save(queryTimeModel);
        queryTimeRepository.save(moreRecentQueryTime);

        // When
        List<QueryTimeModel> mostRecent = queryTimeController.getMostRecentQueryTime();

        // Then
        assertThat(mostRecent, contains(moreRecentQueryTime));
        assertThat(mostRecent, not(contains(queryTimeModel)));
        assertThat(mostRecent.size(), is(1));
        assertThat(mostRecent.get(0).getApplicationName(), is(moreRecentQueryTime.getApplicationName()));
    }

    @Test
    public void testGetQueryTimesForApp() throws Exception {
        // Given
        QueryTimeModel queryTimeModel = new QueryTimeModel("appname", "classname", "methodname", 200L, 0L);
        QueryTimeModel otherQueryTimeModel = new QueryTimeModel("appname2", "classname", "methodname", 200L, 10L);
        queryTimeRepository.save(queryTimeModel);
        queryTimeRepository.save(otherQueryTimeModel);

        // When
        List<QueryTimeModel> queryTimes = queryTimeController.getQueryTimesForApp("appname");

        // Then
        assertThat(queryTimes, contains(queryTimeModel));
        assertThat(queryTimes, Matchers.not(contains(otherQueryTimeModel)));
        assertThat(queryTimes.size(), is(1));
        assertThat(queryTimes.get(0).getApplicationName(), is("appname"));
        assertThat(queryTimes.get(0).getApplicationName(), is(Matchers.not("appname2")));

    }
}