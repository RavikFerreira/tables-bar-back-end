package com.tables.core.controller;

import com.tables.core.models.Event;
import com.tables.core.service.EventService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import jakarta.inject.Inject;

import java.util.List;

@Controller("event/")
@Secured("ADMIN")
public class EventController {

    @Inject
    private EventService eventService;

    @Get("filters/{tableId}")
    public Event findByFilters(@QueryValue String tableId){
        return eventService.findByFilters(tableId);
    }

    @Get("list")
    public List<Event> findAll(){
        return eventService.findAll();
    }
}
