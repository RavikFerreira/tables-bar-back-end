package com.inventory.core.service;

import com.inventory.core.dto.*;
import com.inventory.core.enums.EProductStatus;
import com.inventory.core.kafka.Producer;
import com.inventory.core.models.Inventory;
import com.inventory.core.repository.InventoryRepository;
import com.inventory.core.utils.JsonUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Singleton
@AllArgsConstructor
public class InventoryProductService {
    private static final Logger LOG = LoggerFactory.getLogger(InventoryProductService.class);

    private static final String CURRENT_SOURCE_PRODUCT = "INVENTORY_SERVICE";

    @Inject
    private JsonUtil jsonUtil;
    @Inject
    private Producer producer;
    @Inject
    private InventoryRepository inventoryRepository;

    public void createInventory(EventProduct event){
        try{
//            createInventoryProduct(event);
//            Product product = event.getPayload();
            Inventory orderInventory = createInventoryProduct(event);
            inventoryRepository.update(orderInventory);
            handleSuccess(event);
        }catch (Exception ex) {
            LOG.error("Error trying to add to inventory: " , ex);
            handleFailCurrentNotExecuted(event, ex.getMessage());
        }
            producer.sendEventProduct(jsonUtil.toJson(event));
    }

    private Inventory createInventoryProduct(EventProduct event){
        Inventory inventory = new Inventory();
        inventory.setIdProduct(event.getPayload().getIdProduct());
        inventory.setAvailable(event.getPayload().getQuantity());
        inventory.setOldQuantity(inventory.getAvailable());
        return inventoryRepository.save(inventory);
    }

    private void handleSuccess(EventProduct event){
        event.setStatus(EProductStatus.SUCCESS);
        event.setSource(CURRENT_SOURCE_PRODUCT);
        addHistory(event, "Inventory updated successfully");
    }
    private void addHistory(EventProduct event, String message){
        HistoryProduct history = new HistoryProduct();
        history.setSource(event.getSource());
        history.setStatus(event.getStatus());
        history.setMessage(message);
        history.setCreatedAt(LocalDateTime.now());
        event.addToHistory(history);
    }
    private void handleFailCurrentNotExecuted(EventProduct event, String message){
        event.setStatus(EProductStatus.ROLLBACK_PENDING);
        event.setSource(CURRENT_SOURCE_PRODUCT);
        addHistory(event, "Fail to update inventory: ".concat(message));
    }

    public void rollbackInventory(EventProduct event){
        event.setStatus(EProductStatus.FAIL);
        event.setSource(CURRENT_SOURCE_PRODUCT);
        try{
            returnInventoryToPreviousValues(event);
            addHistory(event, "Rollback executed for inventory! ");

        }catch (Exception ex){
            addHistory(event, "Rollback not executed for inventory! ".concat(ex.getMessage()));
        }
        producer.sendEventProduct(jsonUtil.toJson(event));
    }

    private void returnInventoryToPreviousValues(EventProduct event){
        inventoryRepository.findByIdProduct(event.getPayload().getIdProduct());
        Inventory inventory = new Inventory();
        inventory.setAvailable(inventory.getOldQuantity());
        inventoryRepository.save(inventory);
        LOG.info("Restored inventory for order {} from {} to {}",
                event.getPayload().getIdProduct(), inventory.getNewQuantity(), inventory.getAvailable());
    }
}
