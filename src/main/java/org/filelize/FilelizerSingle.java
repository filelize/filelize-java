package org.filelize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.filelize.file.FileHandler;
import org.filelize.path.PathHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.*;

import static org.filelize.FilelizeUtil.*;

public class FilelizerSingle implements IFilelizer  {
    private final Logger log = LoggerFactory.getLogger(FilelizerSingle.class);
    private final ObjectMapper objectMapper;
    private final PathHandler pathHandler;
    private final FileHandler fileHandler;

    public FilelizerSingle(String basePath) {
        this.objectMapper = new ObjectMapper();
        this.pathHandler = new PathHandler(basePath, FilelizeType.SINGLE_FILE, objectMapper);
        this.fileHandler = new FileHandler(objectMapper);
    }

    public FilelizerSingle(String basePath, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.pathHandler = new PathHandler(basePath, FilelizeType.SINGLE_FILE, objectMapper);
        this.fileHandler = new FileHandler(objectMapper);
    }

    @Override
    public <T> T find(String id, Class<T> valueType) {
        Map<String, T> all = findAll(valueType);
        T t = all.get(id);
        return t;
    }

    @Override
    public <T> Map<String, T> findAll(Class<T> valueType) {
        var fullPath = pathHandler.getFullPath(valueType);
        try {
            return fileHandler.readFileMap(fullPath, valueType);
        } catch (NoSuchFileException e) {
            return new HashMap<>();
        } catch (IOException e) {
            log.error("Error occurred when trying to get " + fullPath, e);
            return new HashMap<>();
        }
    }

    public <T> String save(T object) {
        Map<String, T> objectsToUpdate = findAll((Class<T>) object.getClass());
        var id = getFilelizeId(objectMapper, object);
        objectsToUpdate.put(id, object);
        save2(objectsToUpdate);
        return id;
    }

    public <T> List<String> saveAll(List<T> objects) {
        Map<String, T> objectsToUpdate = findAll((Class<T>) objects.get(0).getClass());
        for(T object : objects) {
            var id = getFilelizeId(objectMapper, object);
            objectsToUpdate.put(id, object);
        }
        var ids = save2(objectsToUpdate);
        return ids;
    }

    @Override
    public <T> void delete(String id, Class<T> valueType) {
        Map<String, T> all = findAll(valueType);
        all.remove(id);
        save2(all);
    }

    public <T> List<String> save2(Map<String, T> objects) {
        try {
            var fullPath = pathHandler.getFullPathOfMap(objects);
            fileHandler.writeFile(fullPath, objects);
            return new ArrayList<>(objects.keySet());
        } catch (IOException e) {
            throw new RuntimeException("Error occurred when trying to open or create a file for writing",e);
        }
    }

}
