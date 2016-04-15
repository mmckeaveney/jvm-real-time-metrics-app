package com.jvm.realtime.controller;

import com.jvm.realtime.JvmRealTimeMetricsJavaApplication;
import com.jvm.realtime.model.AlertModel;
import com.jvm.realtime.persistence.AlertRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JvmRealTimeMetricsJavaApplication.class, EmbeddedMongoAutoConfiguration.class })
@TestPropertySource(locations="classpath:test.properties")
public class AlertControllerTest {

    @Autowired
    private AlertController alertController;
    @Autowired
    private AlertRepository alertRepository;


    @Before
    public void setUp() throws Exception {
        alertRepository.deleteAll();
    }

    @Test
    public void testGetAllAlerts() throws Exception {
        // Given
        AlertModel alertModel = new AlertModel("appName", "classes", "Greater Than", 0, "username", 0L);
        String id = alertModel.getAppName() + alertModel.getMetric() + alertModel.getCondition() + alertModel.getCriteria() + alertModel.getUser();
        alertRepository.save(alertModel);

        // When
        List<AlertModel> alerts = alertController.getAllAlerts();

        // Then
        assertThat(alerts, contains(alertModel));
        assertThat(alerts.size(), is(1));
    }

    @Test
    public void testSaveAlert() throws Exception {
        // Given
        AlertModel alertModel = new AlertModel("appName", "classes", "Greater Than", 0, "username", 0L);
        String id = alertModel.getAppName() + alertModel.getMetric() + alertModel.getCondition() + alertModel.getCriteria() + alertModel.getUser();
        assertThat(alertRepository.findAll().size(), is(0));

        // When
        AlertModel savedAlert = alertController.saveAlert(alertModel);

        // Then
        assertThat(alertRepository.findOne(id), is(savedAlert));
        assertThat(alertRepository.findAll().size(), is(1));
    }

    @Test
    public void testFindAllTriggeredAlerts() throws Exception {
        // Given
        AlertModel alertModel = new AlertModel("appName", "classes", "Greater Than", 0, "username", 0L);
        alertModel.setTriggered(true);
        alertRepository.save(alertModel);

        // When
        List<AlertModel> triggered = alertController.findAllTriggeredAlerts();

        // Then
        assertThat(triggered, contains(alertModel));
        assertThat(triggered.size(), is(1));
        assertThat(triggered.get(0).isTriggered(), is(true));
    }

    @Test
    public void testGetTriggeredAlertsForApp() throws Exception {
        // Given
        AlertModel alertModel = new AlertModel("appName", "classes", "Greater Than", 0, "username", 0L);
        alertModel.setTriggered(true);
        alertRepository.save(alertModel);

        // When
        List<AlertModel> triggered = alertController.getTriggeredAlerts("appName");

        // Then
        assertThat(triggered, contains(alertModel));
        assertThat(triggered.size(), is(1));
        assertThat(triggered.get(0).isTriggered(), is(true));
    }

    @Test
    public void testGetMostRecentAlert() throws Exception {
        // Given
        AlertModel alertModel = new AlertModel("appName", "classes", "Greater Than", 0, "username", 0L);
        AlertModel moreRecentAlert = new AlertModel("appName", "classes", "Greater Than", 0, "username", 100L);
        alertModel.setTriggered(true);
        moreRecentAlert.setTriggered(true);
        alertRepository.save(alertModel);
        alertRepository.save(moreRecentAlert);

        // When
        List<AlertModel> recent = alertController.getMostRecentAlert();

        // Then
        assertThat(recent, contains(moreRecentAlert));
        assertThat(recent.size(), is(1));
        assertThat(recent.get(0).isTriggered(), is(true));
        assertThat(recent.get(0), is(moreRecentAlert));
    }

    @Test
    public void testResetAlert() throws Exception {
        // Given
        AlertModel alertModel = new AlertModel("appName", "classes", "Greater Than", 0, "username", 0L);
        String id = alertModel.getAppName() + alertModel.getMetric() + alertModel.getCondition() + alertModel.getCriteria() + alertModel.getUser();
        alertModel.setTriggered(true);
        alertRepository.save(alertModel);

        // When
        alertController.resetAlert(id);

        // Then
        assertThat(alertRepository.findOne(id).isTriggered(), is(false));
    }

    @Test
    public void testDeleteAlert() throws Exception {
        // Given
        AlertModel alertModel = new AlertModel("appName", "classes", "Greater Than", 0, "username", 0L);
        String id = alertModel.getAppName() + alertModel.getMetric() + alertModel.getCondition() + alertModel.getCriteria() + alertModel.getUser();
        alertRepository.save(alertModel);
        assertThat(alertRepository.findAll().size(), is(1));

        // When
        alertController.deleteAlert(id);

        // Then
        assertThat(alertRepository.findOne(id), is(not(alertModel)));
        assertThat(alertRepository.findAll().size(), is(0));
    }
}