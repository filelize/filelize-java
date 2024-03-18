package org.filelize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.filelize.FilelizeUtil.getFilelizeType;
import static org.filelize.FilelizeUtil.getFilelizeTypeOfList;

public class Filelizer {

    private final Logger log = LoggerFactory.getLogger(Filelizer.class);

    private FilelizerSingle filelizerSingle;
    private FilelizerMultiple filelizerMultiple;

    private final FilelizeType defaultFilelizeType;

    public Filelizer(String path) {
        var objectMapper = new ObjectMapper();
        this.filelizerSingle = new FilelizerSingle(path, new JsonMapper(objectMapper));
        this.filelizerMultiple = new FilelizerMultiple(path, new JsonMapper(objectMapper));
        this.defaultFilelizeType = FilelizeType.SINGLE_FILE;
    }

    public Filelizer(String path, ObjectMapper objectMapper, FilelizeType defaultFilelizeType) {
        this.filelizerSingle = new FilelizerSingle(path, new JsonMapper(objectMapper));
        this.filelizerMultiple = new FilelizerMultiple(path, new JsonMapper(objectMapper));
        this.defaultFilelizeType = defaultFilelizeType;
    }

    public <T> T find(String filename, Class<T> valueType) {
        var filelizeType = getFilelizeType(valueType, defaultFilelizeType);
        if(filelizeType == FilelizeType.SINGLE_FILE) {
            return filelizerSingle.find(filename, valueType);
        }
        return filelizerMultiple.find(filename, valueType);
    }

    public String save(Object object) {
        var filelizeType = getFilelizeType(object, defaultFilelizeType);
        if(filelizeType == FilelizeType.SINGLE_FILE) {
            return filelizerSingle.save(object);
        }
        return filelizerMultiple.save(object);
    }

    public String save(String filename, Object object) {
        var filelizeType = getFilelizeType(object, defaultFilelizeType);
        if(filelizeType == FilelizeType.SINGLE_FILE) {
            return filelizerSingle.save(filename, object);
        }
        return filelizerMultiple.save(filename, object);
    }

    public List<String> saveAll(List<?> objects) {
        var filelizeType = getFilelizeTypeOfList(objects, defaultFilelizeType);
        if(filelizeType == FilelizeType.SINGLE_FILE) {
            return filelizerSingle.saveAll(objects);
        }
        return filelizerMultiple.saveAll(objects);
    }

    public String saveAll(String filename, List<?> objects) {
        var filelizeType = getFilelizeTypeOfList(objects, defaultFilelizeType);
        if(filelizeType == FilelizeType.SINGLE_FILE) {
            return filelizerSingle.saveAll(filename, objects);
        }
        return filelizerMultiple.saveAll(filename, objects);
    }
}
