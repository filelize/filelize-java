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
        objectMapper.findAndRegisterModules();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.filelizerObject = new FilelizerObject(basePath, objectMapper);
        this.filelizerSingle = new FilelizerSingle(basePath, objectMapper);
        this.filelizerMultiple = new FilelizerMultiple(basePath, objectMapper);
        this.defaultFilelizeType = FilelizeType.OBJECT_FILE;
    }

    public Filelizer(String basePath, ObjectMapper objectMapper, FilelizeType defaultFilelizeType) {
        objectMapper.findAndRegisterModules();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.filelizerObject = new FilelizerObject(basePath, objectMapper);
        this.filelizerSingle = new FilelizerSingle(basePath, objectMapper);
        this.filelizerMultiple = new FilelizerMultiple(basePath, objectMapper);
        this.defaultFilelizeType = defaultFilelizeType;
    }

    private IFilelizer resolveFilelizerType(Object valueType) {
        return resolveFilelizerType(getFilelizeType(valueType, defaultFilelizeType));
    }

    private IFilelizer resolveFilelizerType(FilelizeType filelizeType) {
        // I do not like this implementation. It's mandated because of object graph structure and Java 11.
        // With Java 17 or later, this becomes a switch expression and is much nicer.
        IFilelizer filelizer = null;
        switch (filelizeType) {
            case MULTIPLE_FILES:
                filelizer = filelizerMultiple;
                break;
            case SINGLE_FILE:
                filelizer = filelizerSingle;
                break;
            case OBJECT_FILE:
                filelizer = filelizerObject;
                break;
        }
        return filelizer;
    }

    @Override
    public <T> T find(String id, Class<T> valueType) {
        return resolveFilelizerType(valueType).find(id, valueType);
    }

    @Override
    public <T> Map<String, T> findAll(Class<T> valueType) {
        return resolveFilelizerType(valueType).findAll(valueType);
    }

    @Override
    public <T> String save(T object) {
        return resolveFilelizerType(object).save(object);
    }

    @Override
    public <T> String save(String id, T object) {
        return resolveFilelizerType(object).save(id, object);
    }

    @Override
    public <T> List<String> saveAll(List<T> objects) {
        return resolveFilelizerType(getFilelizeTypeOfList(objects, defaultFilelizeType)).saveAll(objects);
    }

    @Override
    public <T> void delete(String id, Class<T> valueType) {
        resolveFilelizerType(valueType).delete(id, valueType);
    }
}
