package org.filelize;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FilelizeUtil {

    public static String getFilelizeName(Object obj) {
        var filelizeAnnotation = obj.getClass().getAnnotation(Filelize.class);
        if (filelizeAnnotation != null) {
            return filelizeAnnotation.name();
        } else {
            return obj.getClass().getSimpleName();
        }
    }

    public static FilelizeType getFilelizeType(Object obj, FilelizeType defaultFilelizeType) {
        var filelizeAnnotation = obj.getClass().getAnnotation(Filelize.class);
        if (filelizeAnnotation != null) {
            return filelizeAnnotation.type();
        } else {
            return defaultFilelizeType;
        }
    }

    public static String getFilelizeId(Object object) {
        Class<?> clazz = object.getClass();
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
        return null;
    }

    private static String getString(Object object, Field field) {
        try {
            return (String) field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
