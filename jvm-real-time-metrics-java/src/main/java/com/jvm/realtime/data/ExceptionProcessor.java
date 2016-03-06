package com.jvm.realtime.data;

import com.jvm.realtime.model.ExceptionModel;
import com.jvm.realtime.persistence.ExceptionRepository;
import com.jvm.realtime.websocket.WebSocketConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class ExceptionProcessor {

    private final SimpMessagingTemplate websocket;
    private final ExceptionRepository exceptionRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionProcessor.class);

    @Autowired
    public ExceptionProcessor(SimpMessagingTemplate websocket, ExceptionRepository exceptionRepository) {
        this.websocket = websocket;
        this.exceptionRepository = exceptionRepository;
    }

    public void notifyAndStoreException(ExceptionModel exceptionModel) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(cal.getTime());

        LOGGER.info("Exception data being saved at " + currentTime);
        exceptionRepository.save(exceptionModel);

        LOGGER.info("Real time exception notification being transmitted over websocket at " + currentTime);
        websocket.convertAndSend(WebSocketConfiguration.MESSAGE_PREFIX + "/exceptionUpdate", exceptionModel);

    }

}
