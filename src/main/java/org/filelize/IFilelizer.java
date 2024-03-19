package org.filelize;

import java.util.List;
import java.util.Map;

public interface IFilelizer {
    <T> T find(String filename, Class<T> valueType);
    <T> Map<String, T> findAll(String folder, Class<T> valueType);
    <T> String save(T object);
    <T> List<String> saveAll(List<T> objects);
}
