package org.filelize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FilelizerBase implements IFilelizer {
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

    public String getPath() {
        return path;
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

    protected String getFullPath(String filename) {
        return path + "/" + filename;
    }
}
