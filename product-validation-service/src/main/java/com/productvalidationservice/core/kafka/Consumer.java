package com.productvalidationservice.core.kafka;

import com.productvalidationservice.core.dto.Event;
import com.productvalidationservice.core.service.ProductValidationService;
import com.productvalidationservice.core.utils.JsonUtil;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@KafkaListener(groupId = "${kafka.consumer.group-id}")
@RequiredArgsConstructor
public class Consumer {
    private static final Logger LOG = LoggerFactory.getLogger(Consumer.class);

    @Inject
    private ProductValidationService productValidationService;
    @Inject
    private JsonUtil jsonUtil;

    @Topic("${kafka.topic.product-validation-success}")
    public void consumerProductValidationSuccessEvent(String payload, @KafkaKey String key){
        if(Objects.equals(key, "1")) {
            LOG.info("Receiving success event {} from product-validation-success topic", payload);
            Event event = jsonUtil.toEvent(payload);
            productValidationService.validateExistingProducts(event);
        }
    }
    @Topic("${kafka.topic.product-validation-fail}")
    public void consumerProductValidationFailEvent(String payload, @KafkaKey String key){
        if(Objects.equals(key, "1")) {
            LOG.info("Receiving rollback event {} from product-validation-fail topic", payload);
            Event event = jsonUtil.toEvent(payload);
            productValidationService.rollbackEvent(event);
        }
    }
}
