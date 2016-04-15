package com.jvm.realtime.data;

import com.jvm.realtime.model.QueryTimeModel;
import com.jvm.realtime.persistence.QueryTimeRepository;
import com.jvm.realtime.websocket.WebSocketConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class QueryTimeProcessorTest {

    @Mock
    private SimpMessagingTemplate websocket;
    @Mock
    private QueryTimeRepository queryTimeRepository;
    @InjectMocks
    private QueryTimeProcessor queryTimeProcessor;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testNotifyAndStoreQueryTime() throws Exception {
        // Given
        QueryTimeModel queryTimeModel = new QueryTimeModel("appname", "classname", "methodname", 200L, 0L);

        // When
        queryTimeProcessor.notifyAndStoreQueryTime(queryTimeModel);

        // Then
        verify(queryTimeRepository, atLeastOnce()).save(queryTimeModel);
        verify(websocket, atLeastOnce()).convertAndSend(WebSocketConfiguration.MESSAGE_PREFIX + "/queryTimeUpdate", queryTimeModel);
    }
}