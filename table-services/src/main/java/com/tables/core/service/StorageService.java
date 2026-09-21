package com.tables.core.service;

import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {

    StoredFile store(String fileName, String contentType, InputStream inputStream) throws IOException;

    InputStream load(String storedFileName) throws IOException;

    void delete(String storedFileName) throws IOException;

    record StoredFile(String storedFileName, long size) {
    }
}
