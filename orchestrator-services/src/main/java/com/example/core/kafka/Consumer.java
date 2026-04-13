package com.example.core.kafka;

import com.example.core.dto.Event;
import com.example.core.dto.EventProduct;
import com.example.core.services.OrchestratorProductService;
import com.example.core.services.OrchestratorService;
import com.example.core.utils.JsonUtil;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@KafkaListener(groupId = "${kafka.consumer.group-id}")
public class Consumer {

    private static final Logger LOG = LoggerFactory.getLogger(Consumer.class);
    @Inject
    private OrchestratorService orchestratorService;
    @Inject
    private OrchestratorProductService orchestratorProductService;
    @Inject
    private JsonUtil jsonUtil;

    @Topic("${kafka.topic.start}")
    public void consumerStartEvent(String payload, @KafkaKey String key){
        if(Objects.equals(key, "1")) {
            LOG.info("Receiving event {} from start topic" , payload);
            Event event = jsonUtil.toEvent(payload);
            orchestratorService.start(event);
        }
        if(Objects.equals(key, "2")) {
            LOG.info("Receiving event {} from start product topic", payload);
            EventProduct eventProduct = jsonUtil.toEventProduct(payload);
            orchestratorProductService.start(eventProduct);
        }
    }
    @Topic("${kafka.topic.orchestrator}")
    public void consumerOrchestratorEvent(String payload, @KafkaKey String key) {
        if (Objects.equals(key, "1")) {
            LOG.info("Receiving event {} from orchestrator topic", payload);
            Event event = jsonUtil.toEvent(payload);
            orchestratorService.continueSaga(event);
        }
        if (Objects.equals(key, "2")) {
            LOG.info("Receiving event {} from orchestrator topic", payload);
            EventProduct eventProduct = jsonUtil.toEventProduct(payload);
            orchestratorProductService.continueSaga(eventProduct);
        }
    }
    @Topic("${kafka.topic.finish-success}")
    public void consumerFinishSuccessEvent(String payload, @KafkaKey String key){
        if(Objects.equals(key, "1")) {
            LOG.info("Receiving event {} from finish-success topic", payload);
            Event event = jsonUtil.toEvent(payload);
            orchestratorService.finishSuccess(event);
        }
        if(Objects.equals(key, "2")) {
            LOG.info("Receiving event {} from finish-success topic", payload);
            EventProduct eventProduct = jsonUtil.toEventProduct(payload);
            orchestratorProductService.finishSuccess(eventProduct);
        }
    }

    @Topic("${kafka.topic.finish-fail}")
    public void consumerFinishFailEvent(String payload, @KafkaKey String key){
        if(Objects.equals(key, "1")) {
            LOG.info("Receiving ending notification event {} from finish-fail topic", payload);
            Event event = jsonUtil.toEvent(payload);
            orchestratorService.finishFail(event);
        }
        if(Objects.equals(key, "2")) {
            LOG.info("Receiving ending notification event {} from finish-fail topic", payload);
            EventProduct eventProduct = jsonUtil.toEventProduct(payload);
            orchestratorProductService.finishFail(eventProduct);
        }
    }
}
