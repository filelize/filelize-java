package io.github.filelize;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
    private final ObjectMapper objectMapper;
    private final FilelizeType defaultFilelizeType;

    public Filelizer(String basePath) {
        this.objectMapper = prepareObjectMapper(new ObjectMapper());
        this.filelizerObject = new FilelizerObject(basePath, this.objectMapper);
        this.filelizerSingle = new FilelizerSingle(basePath, this.objectMapper);
        this.filelizerMultiple = new FilelizerMultiple(basePath, this.objectMapper);
        this.defaultFilelizeType = FilelizeType.OBJECT_FILE;
    }

    public Filelizer(String basePath, ObjectMapper objectMapper, FilelizeType defaultFilelizeType) {
        this.objectMapper = prepareObjectMapper(objectMapper);
        this.filelizerObject = new FilelizerObject(basePath, this.objectMapper);
        this.filelizerSingle = new FilelizerSingle(basePath, this.objectMapper);
        this.filelizerMultiple = new FilelizerMultiple(basePath, this.objectMapper);
        this.defaultFilelizeType = defaultFilelizeType;
    }

    @Override
    public <T> T find(String id, Class<T> valueType) {
        return resolveFilelizerType(valueType).find(id, valueType);
    }

    public <T> T find(String id, TypeReference<T> typeReference) {
        var javaType = objectMapper.getTypeFactory().constructType(typeReference);
        var dispatchClass = resolveDispatchClass(javaType);
        Object fileContent = resolveFilelizerType(dispatchClass).find(id, javaType.getRawClass());
        if (fileContent == null) {
            return null;
        }
        try {
            return objectMapper.convertValue(fileContent, typeReference);
        } catch (IllegalArgumentException e) {
            log.error("Error occurred when trying to deserialize {} as {}", id, javaType, e);
            return null;
        }
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

    private IFilelizer resolveFilelizerType(Object valueType) {
        return resolveFilelizerType(getFilelizeType(valueType, defaultFilelizeType));
    }

    private IFilelizer resolveFilelizerType(FilelizeType filelizeType) {
        return switch(filelizeType) {
            case MULTIPLE_FILES -> filelizerMultiple;
            case SINGLE_FILE -> filelizerSingle;
            case OBJECT_FILE -> filelizerObject;
        };
    }

    private Class<?> resolveDispatchClass(JavaType javaType) {
        if (javaType.isCollectionLikeType() && javaType.getContentType() != null) {
            return resolveDispatchClass(javaType.getContentType());
        }
        if (javaType.isMapLikeType() && javaType.getContentType() != null) {
            return resolveDispatchClass(javaType.getContentType());
        }
        return javaType.getRawClass();
    }

    // enable(MapperFeature...) is deprecated in favor of JsonMapper.builder(), but that only
    // applies when building a mapper from scratch. Since callers may pass in their own
    // already-built ObjectMapper (see the Spring Boot constructor), it must be reconfigured
    // in place, and Jackson provides no non-deprecated way to do that.
    @SuppressWarnings("deprecation")
    private static ObjectMapper prepareObjectMapper(ObjectMapper objectMapper) {
        objectMapper.findAndRegisterModules();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        objectMapper.setAnnotationIntrospector(new FilelizeIdFirstAnnotationIntrospector());
        return objectMapper;
    }
}
