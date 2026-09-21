package com.tables.core.service;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Singleton
@Requires(property = "core.service.type", value = "local", defaultValue = "local")
public class LocalFileStorageService implements StorageService {

    private final Path uploadDir;

    public LocalFileStorageService(@Value("${core.service.local.upload-dir:./uploads}") String uploadDirPath) {
        this.uploadDir = Paths.get(uploadDirPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possível criar o diretório de upload: " + uploadDir, e);
        }
    }

    @Override
    public StoredFile store(String fileName, String contentType, InputStream inputStream) throws IOException {
        String storedFileName = UUID.randomUUID() + extractExtension(fileName);
        Path targetPath = uploadDir.resolve(storedFileName);
        long size = Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        return new StorageService.StoredFile(storedFileName, size);
    }

    @Override
    public InputStream load(String storedFileName) throws IOException {
        Path targetPath = resolveSafely(storedFileName);
        return Files.newInputStream(targetPath);
    }

    @Override
    public void delete(String storedFileName) throws IOException {
        Files.deleteIfExists(resolveSafely(storedFileName));
    }

    private Path resolveSafely(String storedFileName) {
        Path targetPath = uploadDir.resolve(storedFileName).normalize();
        if (!targetPath.startsWith(uploadDir)) {
            throw new SecurityException("Caminho de arquivo inválido: " + storedFileName);
        }
        return targetPath;
    }

    private String extractExtension(String fileName) {
        if (fileName == null) return "";
        int dot = fileName.lastIndexOf('.');
        return dot >= 0 ? fileName.substring(dot) : "";
    }

}







