package com.jvm.realtime.data;

import com.jvm.realtime.model.ExceptionModel;
import com.jvm.realtime.persistence.ExceptionRepository;
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
public class ExceptionProcessorTest {

    @Mock
    private SimpMessagingTemplate websocket;
    @Mock
    private ExceptionRepository exceptionRepository;
    @InjectMocks
    private ExceptionProcessor exceptionProcessor;

    @Test
    public void testNotifyAndStoreException() throws Exception {
        // Given
        ExceptionModel exceptionModel = new ExceptionModel("message", "appname", "method", "class", "type", 0L);

        // When
        exceptionProcessor.notifyAndStoreException(exceptionModel);

        // Then
        verify(exceptionRepository, atLeastOnce()).save(exceptionModel);
        verify(websocket, atLeastOnce()).convertAndSend(WebSocketConfiguration.MESSAGE_PREFIX + "/exceptionsUpdate", exceptionModel);
    }
}