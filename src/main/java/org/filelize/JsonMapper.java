package org.filelize;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonMapper {

    private final Logger log = LoggerFactory.getLogger(JsonMapper.class);

    private final ObjectMapper objectMapper;

    public JsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T readFile(String fullPath, Class<T> valueType) throws IOException {
        JsonNode json = objectMapper.readTree(Files.newBufferedReader(Paths.get(fullPath)));
        return objectMapper.treeToValue(json, valueType);
    }

    public void writeFile(String fullPath, Object object) throws IOException {
        ensureFile(fullPath);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(Files.newBufferedWriter(Paths.get(fullPath)), object);
    }

    private void ensureFile(String fullPath) throws IOException {
        var path = Paths.get(fullPath);
        Files.createDirectories(path.getParent());
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }
}
