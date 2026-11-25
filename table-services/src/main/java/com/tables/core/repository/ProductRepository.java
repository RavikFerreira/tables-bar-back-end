package com.tables.core.repository;

import com.tables.core.models.Product;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@MongoRepository(databaseName = "table-db")
public interface ProductRepository extends CrudRepository<Product, String> {

    List<Product> findAllOrderByCategoryAsc();
    Optional<Product> findByIdProduct (String idProduct);

}
