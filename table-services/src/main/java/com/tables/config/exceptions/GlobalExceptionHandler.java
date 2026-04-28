package com.tables.config.exceptions;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Singleton;


@Produces
@Singleton
public class GlobalExceptionHandler {

    @Error(exception = TablesResourceNotFoundException.class)
    public HttpResponse<StandardError> handleTablesResourceNotFoundException(TablesResourceNotFoundException ex, HttpRequest request) {
        String error = "Não existe mesa com esse id!";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }
    @Error(exception = ProductResourceNotFoundException.class)
    public HttpResponse<StandardError> handleOrderResourceNotFoundException(ProductResourceNotFoundException ex, HttpRequest request) {
        String error = "Não existe pedido com esse id!";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }
    @Error(exception = CannotDeleteABusyTable.class)
    public HttpResponse<StandardError> handleCannotDeleteABusyTable(CannotDeleteABusyTable ex, HttpRequest request) {
        String error = "Não é possível deletar uma mesa ocupada! ";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }
    @Error(exception = UnableToDeleteAnOrderFromATable.class)
    public HttpResponse<StandardError> handleUnableToDeleteAnOrderFromATable(UnableToDeleteAnOrderFromATable ex, HttpRequest request) {
        String error = "Não é possível excluir um pedido de uma mesa! ";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(HttpStatus.NOT_FOUND).body(err);
    }
    @Error(exception = UnableToEditAnOrderFromATable.class)
    public HttpResponse<StandardError> handleUnableToEditAnOrderFromATable(UnableToEditAnOrderFromATable ex, HttpRequest request) {
        String error = "Não é possível editar um pedido de uma mesa! ";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(HttpStatus.NOT_FOUND).body(err);
    }
    @Error(exception = CannotCreateATableWithTheSameId.class)
    public HttpResponse<StandardError> handleCannotCreateATableWithTheSameId(CannotCreateATableWithTheSameId ex, HttpRequest request) {
        String error = "Não é possível criar uma mesa com o mesmo id! ";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status ,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(HttpStatus.NOT_FOUND).body(err);
    }
    @Error(exception = ItIsNotPossibleToAddAProductToTheMenuWithTheSameId.class)
    public HttpResponse<StandardError> handleItIsNotPossibleToAddAProductToTheMenuWithTheSameId(ItIsNotPossibleToAddAProductToTheMenuWithTheSameId ex, HttpRequest request) {
        String error = "Não é possível adicionar ao menu um produto com o mesmo identificador! ";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(HttpStatus.NOT_FOUND).body(err);
    }

    @Error(exception = ItIsNotAllowedToAddOrdersWithTheSameId.class)
    public HttpResponse<StandardError> handleItIsNotAllowedToAddOrdersWithTheSameId (ItIsNotAllowedToAddOrdersWithTheSameId  ex, HttpRequest request) {
        String error = "Não é permitido adicionar pedidos com o mesmo identificador! ";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status,error, ex.getMessage(), request.getPath());
        return HttpResponse.status(HttpStatus.NOT_FOUND).body(err);
    }

    @Error(global = true)
    public HttpResponse<StandardError> handleGenericException(Throwable ex, HttpRequest<?> request) {
        String error = "Ocorreu um erro inesperado!";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(status, error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }
    @Error(exception = PaymentNotRealizedException.class)
    public HttpResponse<StandardError> handlePaymentNotRealizedException(PaymentNotRealizedException ex, HttpRequest request) {
        String error = "Pagamento não realizado!";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status, error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }

    @Error(exception = ProductIsOutOfStock.class)
    public HttpResponse<StandardError> handlerProductIsOutOfStock(ProductIsOutOfStock ex, HttpRequest request) {
        String error = "Produto fora de estoque!";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(status, error, ex.getMessage(), request.getPath());
        return HttpResponse.status(status).body(err);
    }

}
