package com.tables.config.exceptions;

public class ProductIsOutOfStock extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ProductIsOutOfStock(String message) {
        super(message);
    }
}
