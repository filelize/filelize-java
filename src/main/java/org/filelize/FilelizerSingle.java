package org.filelize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.filelize.FilelizeUtil.*;
import static org.filelize.FilelizeUtil.getFilelizeName;

public class FilelizerSingle extends FilelizerBase {

    public FilelizerSingle(String path) {
        super(path);
    }

    public FilelizerSingle(String path, JsonMapper jsonMapper) {
        super(path, jsonMapper);
    }

    public String save(Object object) {
        // todo
        String filename = getFilename(object);
        save(filename, object);
        return filename;
    }

    public List<String> saveAll(List<?> objects) {
        var filename = getFilename(objects);
        Map<String, Object> objectsToUpdate = find(filename, Map.class);
        if(objectsToUpdate == null) {
            objectsToUpdate = new HashMap<>();
        }
        for(Object object : objects) {
            var id = getFilelizeId(object);
            objectsToUpdate.put(id, object);
        }
        var saved = save(objectsToUpdate);
        return List.of(saved);
    }


    private String getFilename(Object obj) {
        var name = getFilelizeName(obj);
        return name + ".json";
    }
}
