package org.filelize;

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

    public <T> Map<String, Object> find(Class<T> valueType) {
        var name = getFilelizeName(valueType);
        var fullPath = getFullPath(name);
        try {
            return jsonMapper.readFile(fullPath, Map.class);
        } catch (IOException e) {
            log.error("Error occurred when trying to get " + fullPath, e);
            return new HashMap<>();
        }
    }

    public String save(Object object) {
        var saved = saveAll(List.of(object));
        return saved.stream().findFirst().orElse("");
    }

    public List<String> saveAll(List<?> objects) {
        var name = getFilelizeNameOfList(objects);
        Map<String, Object> objectsToUpdate = find(name.getClass());
        for(Object object : objects) {
            var id = getFilelizeId(object);
            objectsToUpdate.put(id, object);
        }
        var saved = save(objectsToUpdate);
        return List.of(saved);
    }

}
