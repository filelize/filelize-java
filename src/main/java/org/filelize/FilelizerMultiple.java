package org.filelize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.filelize.file.FileHandler;
import org.filelize.path.PathHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.filelize.FilelizeUtil.*;

public class FilelizerMultiple implements IFilelizer {
    private final Logger log = LoggerFactory.getLogger(FilelizerMultiple.class);
    private final ObjectMapper objectMapper;
    private final PathHandler pathHandler;
    private final FileHandler fileHandler;

    public FilelizerMultiple(String basePath) {
        this.objectMapper = new ObjectMapper();
        this.pathHandler = new PathHandler(basePath, FilelizeType.MULTIPLE_FILES, objectMapper);
        this.fileHandler = new FileHandler(objectMapper);
    }

    public FilelizerMultiple(String basePath, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.pathHandler = new PathHandler(basePath, FilelizeType.MULTIPLE_FILES, objectMapper);
        this.fileHandler = new FileHandler(objectMapper);
    }

    public <T> T find(String id, Class<T> valueType) {
        var fullPath = pathHandler.getFullPath(id, valueType);
        try {
            return fileHandler.readFile(fullPath, valueType);
        } catch (IOException e) {
            log.error("Error occurred when trying to get " + fullPath, e);
            return null;
        }
    }

    public <T> Map<String, T> findAll(Class<T> valueType) {
        var fullPaths = pathHandler.getFullPaths(valueType);
        var objects = new HashMap<String, T>();
        for(var entrySet : fullPaths.entrySet()) {
            var object = readFile(entrySet.getValue(), valueType);
            objects.put(entrySet.getKey(), object);
        }
        return objects;
    }

    public String save(Object object) {
        try {
            var fullPath = pathHandler.getFullPath(object);
            fileHandler.writeFile(fullPath, object);
            return getFilelizeId(objectMapper, object);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred when trying to open or create a file for writing",e);
        }
    }

    public <T> List<String> saveAll(List<T> objects) {
        var filenames = new ArrayList<String>();
        for(var object : objects) {
            String filename = save(object);
            filenames.add(filename);
        }
        return filenames;
    }

    private <T> T readFile(String fullPath, Class<T> valueType) {
        try {
            return fileHandler.readFile(fullPath, valueType);
        } catch (IOException e) {
            log.error("Error occurred when trying to get " + fullPath, e);
            return null;
        }
    }
}
