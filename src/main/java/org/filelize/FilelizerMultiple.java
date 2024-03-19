package org.filelize;

import org.filelize.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.filelize.FilelizeUtil.*;
import static org.filelize.FilelizeUtil.getFilelizeName;

public class FilelizerMultiple extends FilelizerBase {
    private final Logger log = LoggerFactory.getLogger(FilelizerMultiple.class);
    public FilelizerMultiple(String path) {
        super(path);
    }

    public FilelizerMultiple(String path, JsonMapper jsonMapper) {
        super(path, jsonMapper);
    }

    public <T> T find(String filename, Class<T> valueType) {
        var fullPath = getFullPath(filename);
        try {
            return jsonMapper.readFile(fullPath, valueType);
        } catch (IOException e) {
            log.error("Error occurred when trying to get " + fullPath, e);
            return null;
        }
    }

    public <T> Map<String, T> findAll(String folder, Class<T> valueType) {
        return new HashMap<>();
    }

    public String save(Object object) {
        String filename = getFilename(object);
        save(filename, object);
        return filename;
    }

    public <T> List<String> saveAll(List<T> objects) {
        var filenames = new ArrayList<String>();
        for(var object : objects) {
            String filename = save(object);
            filenames.add(filename);
        }
        return filenames;
    }

    private String getFilename(Object obj) {
        var name = getFilelizeName(obj);
        var id = getFilelizeId(obj);
        if(id == null) {
            return name + ".json";
        }
        return name + "_" + id + ".json";
    }
}
