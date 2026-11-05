package com.tables.core.models;

import com.tables.core.models.enums.State;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Serdeable
@MappedEntity
public class TableBar implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private String id;
    private String idTable;
    private Order order;
    private BigDecimal account = BigDecimal.ZERO;

    private State state;

    public TableBar(String id, String idTable, Order order, BigDecimal account, State state) {
        this.id = id;
        this.idTable = idTable;
        this.order = order;
        this.account = account;
        this.state = state;

    }
    public TableBar(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdTable() {
        return idTable;
    }

    public void setIdTable(String idTable) {
        this.idTable = idTable;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getAccount() {
        return account;
    }

    public void setAccount(BigDecimal account) {
        this.account = account;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}

