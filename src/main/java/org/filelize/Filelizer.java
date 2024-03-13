package org.filelize;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Filelizer {

    private final Logger log = LoggerFactory.getLogger(Filelizer.class);
    private final String path;
    private final ObjectMapper objectMapper;

    public Filelizer(String path) {
        this.path = path;
        this.objectMapper = new ObjectMapper();
    }

    public Filelizer(String path, ObjectMapper objectMapper) {
        this.path = path;
        this.objectMapper = objectMapper;
    }

    public <T> T find(String filename, Class<T> valueType) {
        try {
            JsonNode json = objectMapper.readTree(Files.newBufferedReader(getFullPath(filename)));
            return objectMapper.treeToValue(json, valueType);
        } catch (IOException e) {
            log.error("Error occurred when trying to get " + filename, e);
            return null;
        }
    }

    public String save(Object object) {
        var filename = getFilename(object);
        save(filename, object);
        return filename;
    }

    public String save(String filename, Object object) {
        try {
            var fullPath = getFullPath(filename);
            ensureFile(fullPath);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(Files.newBufferedWriter(fullPath), object);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Error occurred when trying to opens or creates a file for writing",e);
        }
    }

    public List<String> saveAll(List<?> objects) {
        var filenames = new ArrayList<String>();
        for(var object : objects) {
            String filename = save(object);
            filenames.add(filename);
        }
        return filenames;
    }

    public String saveAll(String filename, List<Object> objects) {
        return save(filename, objects);
    }

    private void ensureFile(Path fullPath) throws IOException {
        Files.createDirectories(fullPath.getParent());
        if (!Files.exists(fullPath)) {
            Files.createFile(fullPath);
        }
    }

    private Path getFullPath(String filename) {
        var fullPath = path + "/" + filename;
        return Paths.get(fullPath);
    }
    private String getJsonFilename(Object object) {
        Class<?> clazz = object.getClass();
        List<String> jsonFilenameList = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(JsonFilename.class)) {
                jsonFilenameList.add(getString(object, field));
            }
        }
        if(!jsonFilenameList.isEmpty()) {
            return String.join("_", jsonFilenameList);
        }
        return null;
    }

    private String getString(Object object, Field field) {
        try {
            return (String) field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    private String getFilename(Object objectToWrite) {
        var filename = getJsonFilename(objectToWrite);
        if(filename == null) {
            filename = objectToWrite.getClass().getSimpleName();
        }
        filename += ".json";
        return filename;
    }
}
