package org.filelize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.filelize.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.filelize.FilelizeUtil.*;
import static org.filelize.FilelizeUtil.getFilelizeName;

public class FilelizerSingle implements IFilelizer  {
    private final Logger log = LoggerFactory.getLogger(FilelizerSingle.class);

    private final String path;
    private final JsonMapper jsonMapper;

    public FilelizerSingle(String path) {
        this.path = path;
        this.jsonMapper = new JsonMapper(new ObjectMapper());
    }

    public FilelizerSingle(String path, JsonMapper jsonMapper) {
        this.path = path;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public <T> T find(String id, Class<T> valueType) {
        Map<String, T> all = findAll(valueType);
        T t = all.get(id);
        return t;
    }

    @Override
    public <T> Map<String, T> findAll(Class<T> valueType) {
        var fullPath = getFullPath(valueType);
        try {
            return jsonMapper.readFileMap(fullPath, valueType);
        } catch (NoSuchFileException e) {
            return new HashMap<>();
        } catch (IOException e) {
            log.error("Error occurred when trying to get " + fullPath, e);
            return new HashMap<>();
        }
    }

    public <T> String save(T object) {
        var ids = saveAll(List.of(object));
        return ids.stream().findFirst().orElse("");
    }

    public <T> List<String> saveAll(List<T> objects) {
        Map<String, T> objectsToUpdate = findAll((Class<T>) objects.get(0).getClass());
        for(T object : objects) {
            var id = getFilelizeId(object);
            objectsToUpdate.put(id, object);
        }
        var filename = getFilename2(objects);
        var ids = save(filename, objectsToUpdate);
        return ids;
    }

    public <T> List<String> save(String filename, Map<String, T> objects) {
        try {
            var fullPath = path + "/" + filename;
            jsonMapper.writeFile(fullPath, objects);
            return new ArrayList<>(objects.keySet());
        } catch (IOException e) {
            throw new RuntimeException("Error occurred when trying to open or create a file for writing",e);
        }
    }
    
    private String getFullPath(Object object) {
        var filename = getFilename(object);
        return path + "/" + filename;
    }
    private String getFilename(Object obj) {
        var name = getFilelizeName(obj);
        return name + ".json";
    }
    private static <T> String getFilename2(List<T> objects) {
        var name = getFilelizeNameOfList(objects);
        return name + ".json";
    }
}
