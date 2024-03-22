package org.filelize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.filelize.file.FilesUtil;
import org.filelize.json.JsonMapper;
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
    private final String path;
    private final JsonMapper jsonMapper;

    public FilelizerMultiple(String path) {
        this.path = path;
        this.jsonMapper = new JsonMapper(new ObjectMapper());
    }

    public FilelizerMultiple(String path, JsonMapper jsonMapper) {
        this.path = path;
        this.jsonMapper = jsonMapper;
    }

    public <T> T find(String id, Class<T> valueType) {
        var fullPath = getFullPath(id, valueType);
        try {
            return jsonMapper.readFile(fullPath, valueType);
        } catch (IOException e) {
            log.error("Error occurred when trying to get " + fullPath, e);
            return null;
        }
    }

    public <T> Map<String, T> findAll(Class<T> valueType) {
        var filenames = FilesUtil.getFilenames(path);
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
            var fullPath = getFullPath(object);
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

    private <T> String getFullPath(String id, Class<T> valueType) {
        var name = getFilelizeName(valueType);
        return path + "/" + getFilename(id, name);
    }

    private String getFullPath(Object object) {
        var filename = getFilename(object);
        return path + "/" + filename;
    }
    public String getFilename(Object obj) {
        var name = getFilelizeName(obj);
        var id = getFilelizeId(obj);
        return getFilename(id, name);
    }

    private static String getFilename(String id, String name) {
        if(id == null) {
            return name + ".json";
        }
        return name + "_" + id + ".json";
    }
}
