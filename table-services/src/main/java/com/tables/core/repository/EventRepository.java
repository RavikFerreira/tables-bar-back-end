package com.tables.core.repository;

import com.tables.core.models.Event;
import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@MongoRepository(databaseName = "table-db")
public interface EventRepository extends CrudRepository<Event, String> {

    Optional<Event> findTop1ByTableIdOrderByCreatedAtDesc(String tableId);

    List<Event> findAllOrderByCreatedAtDesc();
}
