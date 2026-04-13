package com.tables.core.repository;

import com.tables.core.models.EventProduct;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

@MongoRepository(databaseName = "table-db")
public interface EventProductRepository extends CrudRepository<EventProduct, String> {
}
