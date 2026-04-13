package com.example.core.services;

import com.example.core.dto.EventProduct;
import com.example.core.dto.HistoryProduct;
import com.example.core.enums.EEventSource;
import com.example.core.enums.EStatus;
import com.example.core.enums.ETopic;
import com.example.core.kafka.Producer;
import com.example.core.saga.SagaExecutionProductController;
import com.example.core.utils.JsonUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static com.example.core.enums.EEventSource.ORCHESTRATOR;
import static com.example.core.enums.ETopic.NOTIFY;

@Singleton
@AllArgsConstructor
public class OrchestratorProductService {

    private static final Logger LOG = LoggerFactory.getLogger(OrchestratorProductService.class);

    @Inject
    private JsonUtil jsonUtil;
    @Inject
    private Producer producer;
    @Inject
    private SagaExecutionProductController sagaExecutionProductController;

    public void start(EventProduct event){
        event.setProductSource(EEventSource.ORCHESTRATOR);
        event.setProductStatus(EStatus.SUCCESS);
        ETopic topic = getTopic(event);
        LOG.info("STARTED!");
        addHistory(event, "Started!");
        sendToProducerWithTopic(event,topic);
    }
    public void finishSuccess(EventProduct event){
        event.setProductSource(EEventSource.ORCHESTRATOR);
        event.setProductStatus(EStatus.SUCCESS);
        LOG.info("FINISHED SUCCESSFULLY FOR EVENT {}", event.getId());
        addHistory(event, "Finished successfully!");
        notifyFinished(event);
    }
    public void finishFail(EventProduct event){
        event.setProductSource(EEventSource.ORCHESTRATOR);
        event.setProductStatus(EStatus.FAIL);
        LOG.info("FINISHED WITH ERRORS FOR EVENT {}", event.getId());
        addHistory(event, "Finished with errors!");
        notifyFinished(event);
    }
    public void continueSaga(EventProduct event) {
        ETopic topic = getTopic(event);
        LOG.info("SAGA CONTINUING FOR EVENT {}", event.getId());
        sendToProducerWithTopic(event,topic);
    }

    private ETopic getTopic (EventProduct event){
        return sagaExecutionProductController.getNextTopic(event);
    }

    private void addHistory(EventProduct event, String message){
        HistoryProduct history = new HistoryProduct();
        history.setSource(event.getProductSource());
        history.setStatus(event.getProductStatus());
        history.setMessage(message);
        history.setCreatedAt(LocalDateTime.now());
        event.addToHistory(history);
    }

    private void sendToProducerWithTopic(EventProduct event, ETopic topic){
        producer.sendEventProduct(jsonUtil.toJson(event), topic.getTopic());
    }

    private void notifyFinished(EventProduct event){
        producer.sendEventProduct(jsonUtil.toJson(event), NOTIFY.getTopic());
    }
}
