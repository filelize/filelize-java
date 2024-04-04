package io.github.filelize.file;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static io.github.filelize.FilelizeUtil.getFilelizeId;
import static io.github.filelize.file.FilesUtil.ensureFile;

public class FileHandler {

    private final Logger log = LoggerFactory.getLogger(FileHandler.class);

    private final ObjectMapper objectMapper;

    public FileHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T readFile(String fullPath, Class<T> valueType) throws IOException {
        try {
            JsonNode json = objectMapper.readTree(getNewBufferedReader(fullPath));
            return objectMapper.treeToValue(json, valueType);
        } catch (NoSuchFileException e) {
            return null;
        }
    }

    public <T> Map<String, T> readFileMap(String fullPath, Class<T> valueType) throws IOException {
        try {
            JsonNode json = objectMapper.readTree(getNewBufferedReader(fullPath));
            JavaType mapType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, valueType);
            return objectMapper.readValue(json.traverse(), mapType);
        } catch (NoSuchFileException e) {
            return null;
        }
    }

    public <T> Map<String, Object> readFiles(String folderPath, Class<T> valueType) throws IOException {
        Map<String, Object> content = new HashMap<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    T file1 = readFile(file.getPath(), valueType);
                    String id = getFilelizeId(objectMapper, file1);
                    content.put(id, file1);
                }
            }
        }
        return content;
    }

    public void writeFile(String fullPath, Object object) throws IOException {
        ensureFile(fullPath);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Files.newBufferedWriter(Paths.get(fullPath)), object);
    }

    private static BufferedReader getNewBufferedReader(String fullPath) throws IOException {
        return Files.newBufferedReader(Paths.get(fullPath));
    }

    public void delete(String fullPath) throws IOException {
        try {
            Files.delete(Path.of(fullPath));
        } catch (NoSuchFileException e) {
            log.warn("No such file: " + fullPath);
        }
    }
}
