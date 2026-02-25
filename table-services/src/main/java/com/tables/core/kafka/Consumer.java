package com.tables.core.kafka;

import com.tables.core.models.EventProduct;
import com.tables.core.service.ProductService;
import com.tables.core.utils.JsonUtil;
import com.tables.core.service.EventService;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

@AllArgsConstructor
@KafkaListener(groupId = "${kafka.consumer.group-id}")
public class Consumer {
    private static final Logger LOG = LoggerFactory.getLogger(Consumer.class);

    @Inject
    private EventService eventService;
    @Inject
    private JsonUtil jsonUtil;

    @Topic("${kafka.topic.notify}")
    public void consumerNotifyEvent(String payload, @KafkaKey String key) {
        if (Objects.equals(key, "1")) {
            LOG.info("Receiving ending notification event {} from notify topic", payload);
            var event = jsonUtil.toEvent(payload);
            eventService.notify(event);
        }
        if (Objects.equals(key, "2")) {
            LOG.info("Receiving ending notification event product {} from notify topic", payload);
            var event = jsonUtil.toEventProduct(payload);
            eventService.notifyProduct(event);
        }
    }
}
