package com.tables.core.service;

import com.tables.config.exceptions.TablesResourceNotFoundException;
import com.tables.core.dto.EventFilters;
import com.tables.core.models.Event;
import com.tables.core.models.EventProduct;
import com.tables.core.repository.EventProductRepository;
import com.tables.core.repository.EventRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

import static io.micronaut.core.util.StringUtils.isEmpty;

@Singleton
public class EventService {
    private static final Logger LOG = LoggerFactory.getLogger(EventService.class);

    @Inject
    private EventRepository eventRepository;
    @Inject
    private EventProductRepository eventProductRepository;

    public void notify(Event event){
        event.setTableId(event.getTableId());
        event.setCreatedAt(LocalDateTime.now());
        save(event);
        LOG.info("TableID {} with notified! TransactionID: {} " + event.getTableId(),  event.getTransactionId());
    }
    public void notifyProduct(EventProduct event){
        event.getPayload().setIdProduct(event.getPayload().getIdProduct());
        saveProduct(event);
        LOG.info("ProductID {} with notified! {}", event.getPayload().getIdProduct());
    }
    public List<Event> findAll(){
        return eventRepository.findAllOrderByCreatedAtDesc();
    }

    private Event findByTableId(String tableId){
        return eventRepository.findTop1ByTableIdOrderByCreatedAtDesc(tableId)
                .orElseThrow(() -> new RuntimeException("Event not found by tableID."));
    }


    public Event findByFilters(String tableId){
        validateEmptyFilters(tableId);
        if(!isEmpty(tableId)){
            return findByTableId(tableId);
        }
        else{
            throw new TablesResourceNotFoundException("Table not found");
        }
    }

    private void validateEmptyFilters(String tableId){
        EventFilters filters = new EventFilters();
        filters.setTableId(tableId);
        if(isEmpty(filters.getTableId())){
            throw new RuntimeException("TableId must be informed.");
        }
    }

    public void save(Event event){
        eventRepository.save(event);
    }

    public void saveProduct(EventProduct event){
        eventProductRepository.save(event);
    }
}

