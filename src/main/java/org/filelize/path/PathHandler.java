package org.filelize.path;

import org.filelize.FilelizeType;
import org.filelize.FilelizeUtil;
import org.filelize.file.FilesUtil;

import java.util.HashMap;
import java.util.Map;

import static org.filelize.FilelizeUtil.*;

public class PathHandler {

    private final String basePath;
    private final FilelizeType filelizeType;

    public PathHandler(String basePath, FilelizeType filelizeType) {
        this.basePath = basePath;
        this.filelizeType = filelizeType;
    }

    public String getFullPath(Object object) {
        var filename = getFilename(object);
        return getDirectoryPath(object) + "/" + filename;
    }

    public <T> String getFullPath(String id, Class<T> valueType) {
        if(filelizeType == FilelizeType.MULTIPLE_FILES) {
            var name = getFilelizeName(valueType);
            return getDirectoryPath(valueType) + "/" + getFilename(id, name);
        } else {
            return getFullPath(valueType);
        }
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
            var id = getFilelizeId(obj);
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
        if(object != null) {
            return basePath + "/" + getFilelizeDirectory(object);
        }
        return basePath;
    }

}
