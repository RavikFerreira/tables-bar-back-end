package com.inventory.core.kafka;

import com.inventory.core.dto.Event;
import com.inventory.core.dto.EventProduct;
import com.inventory.core.service.InventoryProductService;
import com.inventory.core.service.InventoryService;
import com.inventory.core.utils.JsonUtil;
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
    private InventoryService inventoryService;
    @Inject
    private InventoryProductService inventoryProductService;
    @Inject
    private JsonUtil jsonUtil;

    @Topic("${kafka.topic.inventory-success}")
    public void consumerInventorySuccessEvent(String payload, @KafkaKey String key){
        if(Objects.equals(key, "1")) {
            LOG.info("Receiving success event {} from inventory-success topic", payload);
            Event event = jsonUtil.toEvent(payload);
            inventoryService.updateInventory(event);
        }
        if(Objects.equals(key, "2")) {
            LOG.info("Receiving success event {} from inventory-success topic", payload);
            EventProduct event = jsonUtil.toEventProduct(payload);
            inventoryProductService.createInventory(event);
        }
    }
    @Topic("${kafka.topic.inventory-fail}")
    public void consumerInventoryFailEvent(String payload, @KafkaKey String key){
        if(Objects.equals(key, "1")) {
            LOG.info("Receiving rollback event {} from inventory-fail topic", payload);
            Event event = jsonUtil.toEvent(payload);
            inventoryService.rollbackInventory(event);
        }
        if(Objects.equals(key, "2")) {
            LOG.info("Receiving rollback event {} from inventory-fail topic", payload);
            EventProduct event = jsonUtil.toEventProduct(payload);
            inventoryProductService.rollbackInventory(event);
        }
    }
}