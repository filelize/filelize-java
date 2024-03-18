package org.filelize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public abstract class FilelizerBase {
    private final Logger log = LoggerFactory.getLogger(FilelizerMultiple.class);
    protected final String path;
    protected final JsonMapper jsonMapper;

    protected FilelizerBase(String path) {
        this.path = path;
        this.jsonMapper = new JsonMapper(new ObjectMapper());
    }

    protected FilelizerBase(String path, JsonMapper jsonMapper) {
        this.path = path;
        this.jsonMapper = jsonMapper;
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

    public String save(String filename, Object object) {
        try {
            var fullPath = getFullPath(filename);
            jsonMapper.writeFile(fullPath, object);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Error occurred when trying to open or create a file for writing",e);
        }
    }

    public String saveAll(String filename, List<?> objects) {
        return save(filename, objects);
    }

    private String getFullPath(String filename) {
        return path + "/" + filename;
    }
}
