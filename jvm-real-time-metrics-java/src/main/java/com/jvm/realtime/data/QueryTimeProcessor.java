package com.jvm.realtime.data;

import com.jvm.realtime.model.QueryTimeModel;
import com.jvm.realtime.persistence.QueryTimeRepository;
import com.jvm.realtime.websocket.WebSocketConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class QueryTimeProcessor {

    private final SimpMessagingTemplate websocket;
    private final QueryTimeRepository queryTimeRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryTimeProcessor.class);

    @Autowired
    public QueryTimeProcessor(SimpMessagingTemplate websocket, QueryTimeRepository queryTimeRepository) {
        this.websocket = websocket;
        this.queryTimeRepository = queryTimeRepository;
    }

    public void notifyAndStoreQueryTime(QueryTimeModel queryTimeModel) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentTime = sdf.format(cal.getTime());

        LOGGER.info("Query Time data being saved at " + currentTime);
        queryTimeRepository.save(queryTimeModel);

        LOGGER.info("Real time query time notification being transmitted over websocket at " + currentTime);
        websocket.convertAndSend(WebSocketConfiguration.MESSAGE_PREFIX + "/queryTimeUpdate", queryTimeModel);
    }
}
