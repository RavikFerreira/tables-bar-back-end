package com.tables.core.service;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.types.files.StreamedFile;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;

import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.NOT_FOUND;

@Singleton
public class UploadImageService {

    private static final Set<String> ALLOWED_TYPES = Set.of("image/png", "image/jpeg", "image/webp", "image/gif");

    private final StorageService storageService;

    public UploadImageService(StorageService storageService) {
        this.storageService = storageService;
    }

    public Map<String, String> upload(CompletedFileUpload file) {
        String contentType = file.getContentType()
                .map(Object::toString)
                .orElse("");

        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new HttpStatusException(BAD_REQUEST, "Tipo de arquivo não permitido: " + contentType);
        }

        try (InputStream in = file.getInputStream()) {
            StorageService.StoredFile stored = storageService.store(file.getFilename(), contentType, in);

            return Map.of("url", "/api/images/" + stored.storedFileName());

        } catch (IOException e) {
            throw new UncheckedIOException("Falha ao salvar arquivo: " + file.getFilename(), e);
        }
    }
    public StreamedFile serve(String filename) {
        try {
            InputStream inputStream = storageService.load(filename);
            String contentType = URLConnection.guessContentTypeFromName(filename);
            MediaType mediaType = contentType != null ? MediaType.of(contentType) : MediaType.APPLICATION_OCTET_STREAM_TYPE;
            return new StreamedFile(inputStream, mediaType);
        } catch (IOException e) {
            throw new HttpStatusException(NOT_FOUND, "Imagem não encontrada: " + filename);
        }
    }
}
