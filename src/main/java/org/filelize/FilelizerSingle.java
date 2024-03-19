package org.filelize;

import org.filelize.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
        var name = getFilelizeName(valueType);
        var fullPath = getFullPath(name);
        try {
            return jsonMapper.readFile(fullPath, valueType);
        } catch (IOException e) {
            log.error("Error occurred when trying to get " + fullPath, e);
            return null;
        }
    }

    @Override
    public <T> Map<String, T> findAll(String folder, Class<T> valueType) {


        return new HashMap<>();
    }

    public <T> String save(T object) {

        //findAll(path, valueType);
        var saved = saveAll(List.of(object));
        return saved.stream().findFirst().orElse("");
    }

    public <T> List<String> saveAll(List<T> objects) {
        var name = getFilelizeNameOfList(objects);
        Map<String, Object> objectsToUpdate = findAll(name, Object.class);
        for(Object object : objects) {
            var id = getFilelizeId(object);
            objectsToUpdate.put(id, object);
        }
        var saved = save(objectsToUpdate);
        return List.of(saved);
    }

}
