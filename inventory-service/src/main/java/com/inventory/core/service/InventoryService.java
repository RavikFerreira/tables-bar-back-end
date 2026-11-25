package com.inventory.core.service;

import com.inventory.core.dto.Event;
import com.inventory.core.dto.History;
import com.inventory.core.dto.Order;
import com.inventory.core.dto.Product;
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

import static com.inventory.core.enums.EStatus.*;

@Singleton
@AllArgsConstructor
public class InventoryService {
    private static final Logger LOG = LoggerFactory.getLogger(InventoryService.class);

    private static final String CURRENT_SOURCE = "INVENTORY_SERVICE";

    @Inject
    private JsonUtil jsonUtil;
    @Inject
    private Producer producer;
    @Inject
    private InventoryRepository inventoryRepository;

    public void updateInventory(Event event){
        try{
            checkCurrentValidation(event);
//            createInventory(event);
            updateInventory(event.getPayload().getOrder());
            handleSuccess(event);
        }catch (Exception ex) {
            LOG.error("Error trying to update inventory: " , ex);
            handleFailCurrentNotExecuted(event, ex.getMessage());
        }
            producer.sendEvent(jsonUtil.toJson(event));
    }

    private void checkCurrentValidation(Event event){
        if(inventoryRepository.existsByTableIdAndTransactionId(event.getPayload().getIdTable(), event.getTransactionId())){
            throw new RuntimeException("There's another transactionId for this validation.");
        }
    }

//    private void createInventory(Event event){
//        event
//                .getPayload()
//                .getOrder()
//                .getProducts().forEach(product -> {
//                    Inventory inventory = findInventoryByIdProduct(product.getIdProduct());
//                    Inventory orderInventory = createInventory(event, product, inventory);
//                    inventoryRepository.update(inventory);
//        } );
//    }
//    private Inventory createInventory(Event event, Product product, Inventory inventory){
//        Inventory inventory1 = new Inventory();
//        inventory1.setOldQuantity(inventory.getAvailable());
//        inventory1.setNewQuantity(inventory.getAvailable() - product.getQuantity());
//        inventory1.setTableId(event.getPayload().getIdTable());
//        inventory1.setTransactionId(event.getTransactionId());
//        return inventoryRepository.save(inventory1);
//    }

    private Inventory findInventoryByIdProduct(String idProduct){
        return inventoryRepository.findByIdProduct(idProduct).orElseThrow(() -> new RuntimeException("Inventory not found informed product"));
    }

    private void updateInventory(Order order){
        order
                .getProducts()
                .forEach(product -> {
                    Inventory inventory = findInventoryByIdProduct(product.getIdProduct());
                    checkInventory(inventory.getAvailable(), product.getQuantity());
                    inventory.setAvailable(inventory.getAvailable() - product.getQuantity());
                    inventoryRepository.update(inventory);
                });

    }
    private void checkInventory(int available, int orderQuantity){
        if(orderQuantity > available){
            throw new RuntimeException("Product is out of stock");
        }

    }

    private void handleSuccess(Event event){
        event.setStatus(SUCCESS);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Inventory updated successfully");
    }
    private void addHistory(Event event, String message){
        History history = new History();
        history.setSource(event.getSource());
        history.setStatus(event.getStatus());
        history.setMessage(message);
        history.setCreatedAt(LocalDateTime.now());
        event.addToHistory(history);
    }
    private void handleFailCurrentNotExecuted(Event event, String message){
        event.setStatus(ROLLBACK_PENDING);
        event.setSource(CURRENT_SOURCE);
        addHistory(event, "Fail to update inventory: ".concat(message));
    }

    public void rollbackInventory(Event event){
        event.setStatus(FAIL);
        event.setSource(CURRENT_SOURCE);
        try{
            returnInventoryToPreviousValues(event);
            addHistory(event, "Rollback executed for inventory! ");

        }catch (Exception ex){
            addHistory(event, "Rollback not executed for inventory! ".concat(ex.getMessage()));
        }
        producer.sendEvent(jsonUtil.toJson(event));
    }

    private void returnInventoryToPreviousValues(Event event){
        inventoryRepository
                .findByTableIdAndTransactionId(event.getPayload().getIdTable(), event.getTransactionId())
                .forEach(inventory -> {
                    Inventory inventory1 = new Inventory();
                    inventory.setAvailable(inventory.getOldQuantity());
                    inventoryRepository.save(inventory1);
                    LOG.info("Restored inventory for order {} from {} to {}",
                            event.getPayload().getIdTable(), inventory.getNewQuantity(), inventory.getAvailable());
                });
    }

}
