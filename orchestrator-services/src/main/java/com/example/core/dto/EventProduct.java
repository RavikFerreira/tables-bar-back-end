package com.example.core.dto;

import com.example.core.enums.EEventSource;
import com.example.core.enums.EStatus;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.ArrayList;
import java.util.List;

import static io.micronaut.core.util.CollectionUtils.isEmpty;

@Serdeable
public class EventProduct {
    private String id;
    private Product payload;
    @Enumerated(EnumType.STRING)
    private EEventSource source;
    @Enumerated(EnumType.STRING)
    private EStatus status;
    private List<HistoryProduct> eventHistory;


    public EventProduct(String id, Product payload, EEventSource source, EStatus status, List<HistoryProduct> eventHistory) {
        this.id = id;
        this.payload = payload;
        this.source = source;
        this.status = status;
        this.eventHistory = eventHistory;
    }

    public EventProduct() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getPayload() {
        return payload;
    }

    public void setPayload(Product payload) {
        this.payload = payload;
    }

    public EEventSource getProductSource() {
        return source;
    }

    public void setProductSource(EEventSource source) {
        this.source = source;
    }

    public EStatus getProductStatus() {
        return status;
    }

    public void setProductStatus(EStatus status) {
        this.status = status;
    }

    public List<HistoryProduct> getEventHistory() {
        return eventHistory;
    }

    public void setEventHistory(List<HistoryProduct> eventHistory) {
        this.eventHistory = eventHistory;
    }

    public void addToHistory(HistoryProduct history){
        if(isEmpty(eventHistory)){
            eventHistory = new ArrayList<>();
        }
        eventHistory.add(history);
    }

}
