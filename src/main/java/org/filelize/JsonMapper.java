package org.filelize;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.filelize.FilelizeUtil.getFilelizeId;
import static org.filelize.FilesUtil.ensureFile;

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

    public <T> Map<String, Object> readFiles(String folderPath, Class<T> valueType) throws IOException {
        Map<String, Object> content = new HashMap<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    T file1 = readFile(file.getPath(), valueType);
                    String id = getFilelizeId(file1);
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
}
