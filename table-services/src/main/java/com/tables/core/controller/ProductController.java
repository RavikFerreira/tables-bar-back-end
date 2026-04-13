package com.tables.core.controller;

import com.tables.config.exceptions.ItIsNotPossibleToAddAProductToTheMenuWithTheSameId;
import com.tables.config.exceptions.UnableToEditAnOrderFromATable;
import com.tables.core.models.Product;
import com.tables.core.service.ProductService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.server.cors.CrossOrigin;
import jakarta.inject.Inject;

import java.util.List;
@Controller("product/")
public class ProductController {

    @Inject
    private ProductService menuService;

    @Get("list")
    public HttpResponse<List<Product>> listProducts(){
        return HttpResponse.ok(menuService.productList());
    }

    @Post("addProduct")
    public HttpResponse<Product> addProduct(@Body Product product) throws ItIsNotPossibleToAddAProductToTheMenuWithTheSameId {
        Product products = menuService.addProduct(product);
        return HttpResponse.created(products);
    }
    @Get("searchProduct/{idProduct}")
    public HttpResponse<Product> search(@PathVariable String idProduct){
        Product product = menuService.searchProduct(idProduct);
        return HttpResponse.ok(product);
    }

    @Patch("updateOrderInProduct/{idProduct}")
    public HttpResponse<Product> updateOrderInProduct(@PathVariable String idProduct, @Body Product product) throws UnableToEditAnOrderFromATable {
        Product products = menuService.updateOrderInProduct(idProduct, product);
        return HttpResponse.ok(products);
    }

    @Delete("delete/{idProduct}")
    public HttpResponse<Product> delete(@PathVariable String idProduct){
        menuService.deleteProduct(idProduct);
        return HttpResponse.ok();
    }
}
