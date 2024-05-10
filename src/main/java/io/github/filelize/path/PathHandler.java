package io.github.filelize.path;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.filelize.file.FilesUtil;
import io.github.filelize.FilelizeType;
import io.github.filelize.FilelizeUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.github.filelize.FilelizeUtil.*;

public class PathHandler {

    private final String basePath;
    private final FilelizeType filelizeType;
    private final ObjectMapper objectMapper;

    public PathHandler(String basePath, FilelizeType filelizeType, ObjectMapper objectMapper) {
        this.basePath = basePath;
        this.filelizeType = filelizeType;
        this.objectMapper = objectMapper;
    }

    public String getFullPath(Object object) {
        var filename = getFilename(object);
        return getDirectoryPath(object) + "/" + filename;
    }

    public String getFullPath2(String id, Object object) {
        return getDirectoryPath(object) + "/" + id + ".json";
    }

    public <T> String getFullPath(String id, Class<T> valueType) {
        if(filelizeType == FilelizeType.MULTIPLE_FILES) {
            var name = getFilelizeName(valueType);
            return getDirectoryPath(valueType) + "/" + getFilename(id, name);
        } else if(filelizeType == FilelizeType.SINGLE_FILE) {
            return getFullPath(valueType);
        }
        return getFullPath2(id, valueType);
    }

    public <T> String getFullPathOfMap(Map<String, T> objects) {
        var filename = getFilelizeNameOfList(objects) + ".json";
        return getDirectoryPath(objects) + "/" + filename;
    }

    public <T> Map<String, String> getFullPaths(Class<T> valueType) {
        var fullPaths = new HashMap<String, String>();
        var filenames = FilesUtil.getFilenames(getDirectoryPath(valueType));
        for(var filename : filenames) {
            var name = FilelizeUtil.getFilelizeName(valueType);
            var id = filename.replace(name+"_", "").replace(".json", "");
            fullPaths.put(id, getFullPath(id, valueType));
        }
        return fullPaths;
    }

    private String getFilename(Object obj) {
        if(filelizeType == FilelizeType.MULTIPLE_FILES) {
            var name = getFilelizeName(obj);
            var id = getFilelizeId(objectMapper, obj);
            return getFilename(id, name);
        } else {
            var name = getFilelizeName(obj);
            return getFilename(null, name);
        }
    }

    private String getFilename(String id, String name) {
        if(id == null) {
            return name + ".json";
        }
        return name + "_" + id + ".json";
    }
    private String getDirectoryPath(Object object){
        Objects.requireNonNull(object);
        return basePath + "/" + getFilelizeDirectory(object);
    }

}
