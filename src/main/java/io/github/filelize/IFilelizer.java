package io.github.filelize;

import java.util.List;
import java.util.Map;

public interface IFilelizer {
    <T> T find(String id, Class<T> valueType);
    <T> Map<String, T> findAll(Class<T> valueType);
    <T> String save(T object);
    <T> String save(String id, T object);
    <T> List<String> saveAll(List<T> objects);
    <T> void delete(String id, Class<T> valueType);
}
