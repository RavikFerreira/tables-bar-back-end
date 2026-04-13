package com.example.core.saga;

import com.example.core.dto.EventProduct;
import com.example.core.enums.EEventSource;
import com.example.core.enums.ETopic;
import io.micronaut.http.annotation.Controller;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.example.core.saga.Handler.*;
import static java.lang.String.format;


@Controller
@AllArgsConstructor
public class SagaExecutionProductController {
    private static final Logger LOG = LoggerFactory.getLogger(SagaExecutionProductController.class);
    private static final String LOG_ID = "PRODUCT ID: %s | EVENT ID %s";

    public ETopic getNextTopic(EventProduct event) {
        if (event.getProductSource() == null || event.getProductStatus() == null) {
            throw new RuntimeException("Source and status must be informed.");
        }
        var topic = findTopicBySourceAndStatus(event);
        logCurrentSaga(event, topic);
        return topic;
    }

    public ETopic findTopicBySourceAndStatus(EventProduct event){
        return (ETopic) Arrays.stream(HANDLER_PRODUCT).filter(row -> isEventSourceAndStatusValid(event, row))
                .map(row -> row[TOPIC_INDEX])
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Topic not found!"));
    }

    private boolean  isEventSourceAndStatusValid(EventProduct event, Object[] row){
        var source = row[EVENT_SOURCE_INDEX];
        var status = row[STATUS_INDEX];
        return source.equals(event.getProductSource()) && status.equals(event.getProductStatus());
    }
    private void logCurrentSaga(EventProduct event, ETopic topic){
        var sagaId = createSagaId(event);
        EEventSource source = event.getProductSource();
        switch (event.getProductStatus()){
            case SUCCESS -> LOG.info("### CURRENT SAGA: {} | SUCCESS | NEXT TOPIC {} | {}"
                    , source, topic, sagaId);
            case ROLLBACK_PENDING -> LOG.info("### CURRENT SAGA: {} | SENDING TO ROLLBACK CURRENT SERVICE | NEXT TOPIC {} | {}"
                    , source, topic, sagaId);
            case FAIL -> LOG.info("### CURRENT SAGA: {} | SENDING TO ROLLBACK PREVIOUS SERVICE | NEXT TOPIC {} | {}"
                    , source, topic, sagaId);
        }
    }

    private String createSagaId(EventProduct event){
        return format(LOG_ID, event.getPayload().getId(), event.getId());
    }
}
