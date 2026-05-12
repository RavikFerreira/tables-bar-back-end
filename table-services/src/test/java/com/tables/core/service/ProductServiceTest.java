package com.tables.core.service;
import com.tables.config.exceptions.ItIsNotPossibleToAddAProductToTheMenuWithTheSameId;
import com.tables.core.kafka.Producer;
import com.tables.core.models.Product;
import com.tables.core.models.enums.Category;
import com.tables.core.repository.ProductRepository;
import com.tables.core.utils.JsonUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    ProductRepository repository =
            mock(ProductRepository.class);
    Producer producer =
            mock(Producer.class);
    JsonUtil jsonUtil =
            mock(JsonUtil.class);
    EventService eventService =
            mock(EventService.class);

    @Test
    void shouldReturnProductListOrderedByCategory() {
        List<Product> products = List.of(
                new Product("1","1", "AGUA 250ML", 2.5, 10, Category.BEBIDA, null),
                new Product("2","2", "COCA-COLA LATA 350ML", 5.0, 10, Category.BEBIDA, null)
        );
        when(repository.findAllOrderByCategoryAsc())
                .thenReturn(products);
        ProductService service =  new ProductService(repository, producer, jsonUtil, eventService);
        List<Product> result = service.productList();
        assertEquals(2, result.size());
        verify(repository)
                .findAllOrderByCategoryAsc();
    }

    @Test
    void shouldThrowExceptionWhenProductAlreadyExists() {
        Product product = new Product();
        product.setIdProduct("1");

        when(repository.findByIdProduct("1"))
                .thenReturn(Optional.of(product));
        ProductService service =
                new ProductService(repository, producer, jsonUtil, eventService);
        assertThrows(
                ItIsNotPossibleToAddAProductToTheMenuWithTheSameId.class,
                () -> service.addProduct(product)
        );
    }

    @Test
    void shouldSaveProductSuccessfully() throws Exception {
        Product product = new Product();
        product.setIdProduct("1");

        when(repository.findByIdProduct("1"))
                .thenReturn(Optional.empty());
        ProductService service =
                new ProductService(repository, producer, jsonUtil, eventService);
        Product result = service.addProduct(product);
        assertNotNull(result);
        verify(repository).save(product);
        verify(producer)
                .sendEventProduct(any());
    }

}
