package com.example.core.dto;


import io.micronaut.serde.annotation.Serdeable;

import java.io.Serial;
import java.io.Serializable;

@Serdeable
public class TableBar implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String idTable;
    private Order order;
    private double account = 0.0;
    private State state;

    public TableBar(String id, String idTable, Order order, double account, State state) {
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

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}

