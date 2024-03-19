package org.filelize;

import org.filelize.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.filelize.FilelizeUtil.*;
import static org.filelize.FilelizeUtil.getFilelizeName;

public class FilelizerSingle extends FilelizerBase {
    private final Logger log = LoggerFactory.getLogger(FilelizerSingle.class);
    public FilelizerSingle(String path) {
        super(path);
    }

    public FilelizerSingle(String path, JsonMapper jsonMapper) {
        super(path, jsonMapper);
    }

    @Override
    public <T> T find(String filename, Class<T> valueType) {
        Map<String, T> all = findAll(filename, valueType);
        var id = getFilelizeId(valueType);
        T t = all.get(id);
        return t;
    }

    @Override
    public <T> Map<String, T> findAll(String folder, Class<T> valueType) {
        var name = getFilelizeName(valueType);
        var fullPath = getFullPath(name);
        try {
            return jsonMapper.readFile(fullPath, Map.class);
        } catch (NoSuchFileException e) {
            return new HashMap<>();
        } catch (IOException e) {
            log.error("Error occurred when trying to get " + fullPath, e);
            return new HashMap<>();
        }
    }

    public <T> String save(T object) {
        //findAll(path, valueType);
        var saved = saveAll(List.of(object));
        return saved.stream().findFirst().orElse("");
    }

    public <T> List<String> saveAll(List<T> objects) {
        var name = getFilelizeNameOfList(objects);
        Map<String, T> objectsToUpdate = findAll(name, (Class<T>) objects.get(0).getClass());
        for(T object : objects) {
            var id = getFilelizeId(object);
            objectsToUpdate.put(id, object);
        }
        var saved = save(name + ".json", objectsToUpdate);
        return List.of(saved);
    }

}
