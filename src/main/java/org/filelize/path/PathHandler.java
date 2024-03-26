package org.filelize.path;

import org.filelize.FilelizeType;
import org.filelize.file.FilesUtil;

import java.util.List;
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
        var name = getFilelizeName(valueType);
        return getDirectoryPath(valueType) + "/" + getFilename(id, name);
    }

    public <T> String getFullPathOfMap(Map<String, T> objects) {
        var filename = getFilename2(objects);
        return getDirectoryPath(objects) + "/" + filename;
    }

    public String getFilename(Object obj) {
        if(filelizeType == FilelizeType.MULTIPLE_FILES) {
            var name = getFilelizeName(obj);
            var id = getFilelizeId(obj);
            return getFilename(id, name);
        } else {
            var name = getFilelizeName(obj);
            return getFilename(null, name);
        }
    }
    public <T> String getFilename2(Map<String, T> objects) {
        var name = getFilelizeNameOfList(objects);
        return name + ".json";
    }
    public <T> List<String> getFilenames(Class<T> valueType) {
        return FilesUtil.getFilenames(getDirectoryPath(valueType));
    }

    public String getFilename(String id, String name) {
        if(id == null) {
            return name + ".json";
        }
        return name + "_" + id + ".json";
    }
    public String getDirectoryPath(Object object){
        if(object != null) {
            return basePath + "/" + getFilelizeDirectory(object);
        }
        return basePath;
    }

}
