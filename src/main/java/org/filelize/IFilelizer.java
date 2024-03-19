package org.filelize;

import java.util.List;
import java.util.Map;

public interface IFilelizer {
    <T> T find(String filename, Class<T> valueType);
    <T> Map<String, Object> findAll(String folder, Class<T> valueType);
    String save(Object object);
    List<String> saveAll(List<?> objects);
}
