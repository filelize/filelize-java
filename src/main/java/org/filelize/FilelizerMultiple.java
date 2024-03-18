package org.filelize;

import java.util.ArrayList;
import java.util.List;

import static org.filelize.FilelizeUtil.*;
import static org.filelize.FilelizeUtil.getFilelizeName;

public class FilelizerMultiple extends FilelizerBase {

    public FilelizerMultiple(String path) {
        super(path);
    }

    public FilelizerMultiple(String path, JsonMapper jsonMapper) {
        super(path, jsonMapper);
    }

    public String save(Object object) {
        String filename = getFilename(object);
        save(filename, object);
        return filename;
    }

    public List<String> saveAll(List<?> objects) {
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
