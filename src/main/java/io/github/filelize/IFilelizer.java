package io.github.filelize;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface IFilelizer {
    <T> T find(String id, Class<T> valueType);
    <T> Map<String, T> findAll(Class<T> valueType);
    <T> String save(T object);
    <T> String save(String id, T object);
    <T> List<String> saveAll(List<T> objects);
    <T> void delete(String id, Class<T> valueType);

    default <T> T sync(String id, Class<T> valueType, ZonedDateTime syncIfOlderThan,
                       Function<T, ZonedDateTime> dateTimeExtractor, Function<String, T> externalLoader) {
        T local = find(id, valueType);
        if (local != null) {
            ZonedDateTime localDateTime = dateTimeExtractor.apply(local);
            if (localDateTime != null && !localDateTime.isBefore(syncIfOlderThan)) {
                return local;
            }
        }

        T external = externalLoader.apply(id);
        if (external != null) {
            save(id, external);
            return external;
        }
        return local;
    }
}
