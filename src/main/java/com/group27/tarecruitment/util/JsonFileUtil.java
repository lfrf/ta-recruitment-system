package com.group27.tarecruitment.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

public final class JsonFileUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .findAndRegisterModules()
            .enable(SerializationFeature.INDENT_OUTPUT);
    private static final Path RUNTIME_DATA_DIR = initialiseRuntimeDataDirectory();

    private JsonFileUtil() {
    }

    public static <T> List<T> readList(String resourcePath, Class<T> elementClass) {
        Path filePath = resolveRuntimeFile(resourcePath);
        if (Files.notExists(filePath)) {
            return Collections.emptyList();
        }

        JavaType listType = MAPPER.getTypeFactory().constructCollectionType(List.class, elementClass);
        try {
            return MAPPER.readValue(filePath.toFile(), listType);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read JSON file: " + filePath, exception);
        }
    }

    public static <T> T readObject(String resourcePath, Class<T> targetClass) {
        Path filePath = resolveRuntimeFile(resourcePath);
        if (Files.notExists(filePath)) {
            throw new IllegalStateException("JSON file not found: " + filePath);
        }

        try {
            return MAPPER.readValue(filePath.toFile(), targetClass);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read JSON file: " + filePath, exception);
        }
    }

    public static <T> void writeList(String resourcePath, List<T> data) {
        writeValue(resourcePath, data);
    }

    public static <T> void writeObject(String resourcePath, T data) {
        writeValue(resourcePath, data);
    }

    public static Path getRuntimeDataDirectory() {
        return RUNTIME_DATA_DIR;
    }

    private static <T> void writeValue(String resourcePath, T data) {
        Path filePath = resolveRuntimeFile(resourcePath);
        try {
            Files.createDirectories(filePath.getParent());
            MAPPER.writeValue(filePath.toFile(), data);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to write JSON file: " + filePath, exception);
        }
    }

    private static Path resolveRuntimeFile(String resourcePath) {
        String fileName = Path.of(resourcePath).getFileName().toString();
        Path filePath = RUNTIME_DATA_DIR.resolve(fileName);
        if (Files.notExists(filePath)) {
            copyFromClasspath(resourcePath, filePath);
        }
        return filePath;
    }

    private static Path initialiseRuntimeDataDirectory() {
        String configuredPath = System.getProperty("ta.recruitment.dataDir");
        Path dataDir;
        if (configuredPath != null && !configuredPath.isBlank()) {
            dataDir = Path.of(configuredPath);
        } else {
            dataDir = Path.of(System.getProperty("user.home"), ".ta-recruitment-system", "data");
        }

        try {
            Files.createDirectories(dataDir);
            return dataDir;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to prepare runtime data directory: " + dataDir, exception);
        }
    }

    private static void copyFromClasspath(String resourcePath, Path targetPath) {
        try (InputStream inputStream = JsonFileUtil.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                return;
            }
            Files.createDirectories(targetPath.getParent());
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to seed runtime JSON file: " + targetPath, exception);
        }
    }
}
