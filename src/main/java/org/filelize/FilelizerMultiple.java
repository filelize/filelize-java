package org.filelize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.filelize.json.JsonMapper;
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
    private final PathHandler pathHandler;
    private final JsonMapper jsonMapper;

    public FilelizerMultiple(String basePath) {
        this.pathHandler = new PathHandler(basePath, FilelizeType.MULTIPLE_FILES);
        this.jsonMapper = new JsonMapper(new ObjectMapper());
    }

    public FilelizerMultiple(String basePath, JsonMapper jsonMapper) {
        this.pathHandler = new PathHandler(basePath, FilelizeType.MULTIPLE_FILES);
        this.jsonMapper = jsonMapper;
    }

    public <T> T find(String id, Class<T> valueType) {
        var fullPath = pathHandler.getFullPath(id, valueType);
        try {
            return jsonMapper.readFile(fullPath, valueType);
        } catch (IOException e) {
            log.error("Error occurred when trying to get " + fullPath, e);
            return null;
        }
    }

    public <T> Map<String, T> findAll(Class<T> valueType) {
        var filenames = pathHandler.getFilenames(valueType);
        var objects = new HashMap<String, T>();
        for(var filename : filenames) {
            var name = FilelizeUtil.getFilelizeName(valueType);
            var id = filename.replace(name+"_", "").replace(".json", "");
            var object = find(id, valueType);
            objects.put(id, object);
        }
        return objects;
    }

    public String save(Object object) {
        try {
            var fullPath = pathHandler.getFullPath(object);
            jsonMapper.writeFile(fullPath, object);
            return getFilelizeId(object);
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
}
