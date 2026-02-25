package com.tables.core.controller;


import com.tables.config.exceptions.CannotCreateATableWithTheSameId;
import com.tables.config.exceptions.CannotDeleteABusyTable;
import com.tables.config.exceptions.ProductResourceNotFoundException;
import com.tables.config.exceptions.TablesResourceNotFoundException;
import com.tables.core.models.TableBar;
import com.tables.core.service.TableService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

import java.util.List;

@Controller("tables/")
public class TableController {
    @Inject
    private TableService tableService;

    @Get("list")
    public HttpResponse<List<TableBar>> listOrders(){
        return HttpResponse.ok(tableService.list());
    }

    @Post("create")
    public HttpResponse<TableBar> addTables(@Body TableBar tableBar) throws TablesResourceNotFoundException, CannotCreateATableWithTheSameId {
        TableBar addTables = tableService.addTables(tableBar);
        return HttpResponse.created(addTables);
    }

    @Patch("orders/{idTable}")
    public HttpResponse<TableBar> addOrder(@PathVariable String idTable){
        TableBar addOrder = tableService.addOrder(idTable);
        return HttpResponse.ok(addOrder);
    }
    @Patch("addProductInOrder/{idTable}/{idProduct}")
    public HttpResponse<TableBar> addProductInOrder(@PathVariable String idTable, @PathVariable String idProduct) throws ProductResourceNotFoundException {
        TableBar product = tableService.addProductInOrder(idTable, idProduct);
        return HttpResponse.created(product);
    }
    @Get("finallyOrder/{idTable}")
    public HttpResponse<TableBar> finalizedOrder(@PathVariable String idTable){
        tableService.finalizedOrder(idTable);
        return HttpResponse.ok();
    }

    @Get("search/{idTable}")
    public HttpResponse<TableBar> search(@PathVariable String idTable){
        TableBar tables = tableService.search(idTable);
        return HttpResponse.ok(tables);
    }

    @Delete("delete/{idTable}")
    public HttpResponse<TableBar> delete(@PathVariable String idTable) throws CannotDeleteABusyTable {
        tableService.delete(idTable);
        return HttpResponse.ok();
    }

    @Get("payment/{idTable}")
    public HttpResponse<TableBar> realizedPayment(@PathVariable String idTable){
        tableService.realizedPayment(idTable);
        return HttpResponse.ok();
    }
    @Get("finallyTables/{idTable}")
    public HttpResponse<TableBar> finalizedTable(@PathVariable String idTable){
        tableService.finalizedTable(idTable);
        return HttpResponse.ok();
    }
}
