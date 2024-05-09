package io.github.filelize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static io.github.filelize.FilelizeUtil.getFilelizeType;
import static io.github.filelize.FilelizeUtil.getFilelizeTypeOfList;

public class Filelizer implements IFilelizer {

    private final Logger log = LoggerFactory.getLogger(Filelizer.class);

    private final FilelizerObject filelizerObject;
    private final FilelizerSingle filelizerSingle;
    private final FilelizerMultiple filelizerMultiple;
    private final FilelizeType defaultFilelizeType;

    public Filelizer(String basePath) {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.filelizerObject = new FilelizerObject(basePath, objectMapper);
        this.filelizerSingle = new FilelizerSingle(basePath, objectMapper);
        this.filelizerMultiple = new FilelizerMultiple(basePath, objectMapper);
        this.defaultFilelizeType = FilelizeType.OBJECT_FILE;
    }

    public Filelizer(String basePath, ObjectMapper objectMapper, FilelizeType defaultFilelizeType) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.filelizerObject = new FilelizerObject(basePath, objectMapper);
        this.filelizerSingle = new FilelizerSingle(basePath, objectMapper);
        this.filelizerMultiple = new FilelizerMultiple(basePath, objectMapper);
        this.defaultFilelizeType = defaultFilelizeType;
    }

    @Override
    public <T> T find(String id, Class<T> valueType) {
        var filelizeType = getFilelizeType(valueType, defaultFilelizeType);
        if(filelizeType == FilelizeType.SINGLE_FILE) {
            return filelizerSingle.find(id, valueType);
        } else if(filelizeType == FilelizeType.MULTIPLE_FILES) {
            return filelizerMultiple.find(id, valueType);
        }
        return filelizerObject.find(id, valueType);
    }

    @Override
    public <T> Map<String, T> findAll(Class<T> valueType) {
        var filelizeType = getFilelizeType(valueType, defaultFilelizeType);
        if(filelizeType == FilelizeType.SINGLE_FILE) {
            return filelizerSingle.findAll(valueType);
        } else if(filelizeType == FilelizeType.MULTIPLE_FILES) {
            return filelizerMultiple.findAll(valueType);
        }
        return filelizerObject.findAll(valueType);
    }

    @Override
    public <T> String save(T object) {
        var filelizeType = getFilelizeType(object, defaultFilelizeType);
        if(filelizeType == FilelizeType.SINGLE_FILE) {
            return filelizerSingle.save(object);
        } else if(filelizeType == FilelizeType.MULTIPLE_FILES) {
            return filelizerMultiple.save(object);
        }
        return filelizerObject.save(object);
    }

    @Override
    public <T> String save(String id, T object) {
        var filelizeType = getFilelizeType(object, defaultFilelizeType);
        if(filelizeType == FilelizeType.SINGLE_FILE) {
            return filelizerSingle.save(id, object);
        } else if(filelizeType == FilelizeType.MULTIPLE_FILES) {
            return filelizerMultiple.save(id, object);
        }
        return filelizerObject.save(id, object);
    }

    @Override
    public <T> List<String> saveAll(List<T> objects) {
        var filelizeType = getFilelizeTypeOfList(objects, defaultFilelizeType);
        if(filelizeType == FilelizeType.SINGLE_FILE) {
            return filelizerSingle.saveAll(objects);
        } else if(filelizeType == FilelizeType.MULTIPLE_FILES) {
            return filelizerMultiple.saveAll(objects);
        }
        return filelizerObject.saveAll(objects);
    }

    @Override
    public <T> void delete(String id, Class<T> valueType) {
        var filelizeType = getFilelizeType(valueType, defaultFilelizeType);
        if(filelizeType == FilelizeType.SINGLE_FILE) {
            filelizerSingle.delete(id, valueType);
        } else if(filelizeType == FilelizeType.MULTIPLE_FILES) {
            filelizerMultiple.delete(id, valueType);
        } else {
            filelizerObject.delete(id, valueType);
        }
    }
}
