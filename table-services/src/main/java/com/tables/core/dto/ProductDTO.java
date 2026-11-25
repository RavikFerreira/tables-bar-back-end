package com.tables.core.dto;

import com.tables.core.models.Product;
import com.tables.core.models.enums.Category;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Serdeable
@NoArgsConstructor
public class ProductDTO {

    private String idProduct;
    private String name;
    private BigDecimal price;
    private int quantity;
    private Category category;
    private String description;

    public ProductDTO(Product product) {
        this.idProduct = product.getIdProduct();
        this.description = product.getDescription();
        this.category = product.getCategory();
        this.quantity = product.getQuantity();
        this.price = product.getPrice();
        this.name = product.getName();
    }
}
