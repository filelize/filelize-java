package io.github.filelize;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.filelize.file.FileHandler;
import io.github.filelize.path.PathHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilelizerObject implements IFilelizer  {
    private final Logger log = LoggerFactory.getLogger(FilelizerObject.class);
    private final ObjectMapper objectMapper;
    private final PathHandler pathHandler;
    private final FileHandler fileHandler;

    public FilelizerObject(String basePath, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.pathHandler = new PathHandler(basePath, FilelizeType.OBJECT_FILE, objectMapper);
        this.fileHandler = new FileHandler(objectMapper);
    }

    @Override
    public <T> T find(String id, Class<T> valueType) {
        var fullPath = pathHandler.getFullPath(id, valueType);
        try {
            return fileHandler.readFile(fullPath, valueType);
        } catch (IOException e) {
            log.error(String.format("Error occurred when trying to get %s", fullPath), e);
            return null;
        }
    }

    @Override
    public <T> Map<String, T> findAll(Class<T> valueType) {
        List<T> t = find(valueType.getSimpleName() + "_all",  List.class);
        Map<String, T> resultMap = t.stream()
                .collect(Collectors.toMap(Object::toString, item -> item));
        return resultMap;
    }

    @Override
    public <T> String save(T object) {
        return save(object.getClass().getSimpleName(), object);
    }

    @Override
    public <T> String save(String id, T object) {
        try {
            var fullPath = pathHandler.getFullPath2(id, object);
            fileHandler.writeFile(fullPath, object);
            return id;
        } catch (IOException e) {
            throw new RuntimeException("Error occurred when trying to open or create a file for writing",e);
        }
    }

    @Override
    public <T> List<String> saveAll(List<T> objects) {
        var object = objects.stream().findFirst().orElse((T) objects);
        String id = save(object.getClass().getSimpleName()+"_all", objects);
        return List.of(id);
    }

    @Override
    public <T> void delete(String id, Class<T> valueType) {
        var fullPath = pathHandler.getFullPath(id, valueType);
        try {
            fileHandler.delete(fullPath);
        } catch (IOException e) {
            log.error(String.format("Error occurred when trying to delete %s", fullPath), e);
        }
    }
}
