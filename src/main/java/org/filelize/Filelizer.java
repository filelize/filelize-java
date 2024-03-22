package org.filelize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.filelize.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.filelize.FilelizeUtil.getFilelizeType;
import static org.filelize.FilelizeUtil.getFilelizeTypeOfList;

public class Filelizer implements IFilelizer{

    private final Logger log = LoggerFactory.getLogger(Filelizer.class);

    private final String path;
    private FilelizerSingle filelizerSingle;
    private FilelizerMultiple filelizerMultiple;

    private final FilelizeType defaultFilelizeType;

    public Filelizer(String path) {
        this.path = path;
        var objectMapper = new ObjectMapper();
        this.filelizerSingle = new FilelizerSingle(path, new JsonMapper(objectMapper));
        this.filelizerMultiple = new FilelizerMultiple(path, new JsonMapper(objectMapper));
        this.defaultFilelizeType = FilelizeType.SINGLE_FILE;
    }

    public Filelizer(String path, ObjectMapper objectMapper, FilelizeType defaultFilelizeType) {
        this.path = path;
        this.filelizerSingle = new FilelizerSingle(path, new JsonMapper(objectMapper));
        this.filelizerMultiple = new FilelizerMultiple(path, new JsonMapper(objectMapper));
        this.defaultFilelizeType = defaultFilelizeType;
    }

    public String getPath() {
        return path;
    }

    public <T> T find(String id, Class<T> valueType) {
        var filelizeType = getFilelizeType(valueType, defaultFilelizeType);
        if(filelizeType == FilelizeType.SINGLE_FILE) {
            return filelizerSingle.find(id, valueType);
        }
        return filelizerMultiple.find(id, valueType);
    }

    @Override
    public <T> Map<String, T> findAll(Class<T> valueType) {
        var filelizeType = getFilelizeType(valueType, defaultFilelizeType);
        if(filelizeType == FilelizeType.SINGLE_FILE) {
            return filelizerSingle.findAll(valueType);
        }
        return filelizerMultiple.findAll(valueType);
    }

    public <T> String save(T object) {
        var filelizeType = getFilelizeType(object, defaultFilelizeType);
        if(filelizeType == FilelizeType.SINGLE_FILE) {
            return filelizerSingle.save(object);
        }
        return filelizerMultiple.save(object);
    }

    public <T> String save(String filename, T object) {
        var filelizeType = getFilelizeType(object, defaultFilelizeType);
        if(filelizeType == FilelizeType.SINGLE_FILE) {
            return filelizerSingle.save(object);
        }
        return filelizerMultiple.save(object);
    }

    public <T> List<String> saveAll(List<T> objects) {
        var filelizeType = getFilelizeTypeOfList(objects, defaultFilelizeType);
        if(filelizeType == FilelizeType.SINGLE_FILE) {
            return filelizerSingle.saveAll(objects);
        }
        return filelizerMultiple.saveAll(objects);
    }
}
