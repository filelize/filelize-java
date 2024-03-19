package org.filelize;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilelizeUtil {


    public static String getFilelizeNameOfList(List<?> objects) {
        var filelizeNameOptional = objects.stream()
                .map(FilelizeUtil::getFilelizeName)
                .findFirst();
        return filelizeNameOptional.orElse("");
    }

    public static String getFilelizeName(Object obj) {
        var clazz = getClazz(obj);
        var filelizeAnnotation = clazz.getAnnotation(Filelize.class);
        if (filelizeAnnotation != null) {
            return filelizeAnnotation.name();
        } else {
            return obj.getClass().getSimpleName();
        }
    }

    public static FilelizeType getFilelizeTypeOfList(List<?> objects, FilelizeType defaultFilelizeType) {
        var fileTypeOptional = objects.stream()
                .map(obj -> getFilelizeType(obj, defaultFilelizeType))
                .findFirst();
        return fileTypeOptional.orElse(defaultFilelizeType);
    }

    public static FilelizeType getFilelizeType(Object obj, FilelizeType defaultFilelizeType) {
        var clazz = getClazz(obj);
        var filelizeAnnotation = clazz.getAnnotation(Filelize.class);
        if (filelizeAnnotation != null) {
            return filelizeAnnotation.type();
        } else {
            return defaultFilelizeType;
        }
    }

    public static String getFilelizeId(Object object) {
        Class<?> clazz = getClazz(object);
        List<String> jsonFilenameList = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                jsonFilenameList.add(getString(object, field));
            }
        }
        if(!jsonFilenameList.isEmpty()) {
            return String.join("_", jsonFilenameList);
        }
        return String.valueOf(object.hashCode());
    }

    private static Class<?> getClazz(Object object) {
        if (object instanceof Class<?>) {
            return (Class<?>) object;
        } else if(object instanceof Map) {
            var optional = (((Map<?, ?>) object).values().stream().findFirst());
            if(optional.isPresent()) {
                return optional.get().getClass();
            }
        }
        return object.getClass();
    }

    private static String getString(Object object, Field field) {
        try {
            return (String) field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
