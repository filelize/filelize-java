package org.filelize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilelizeUtil {


    public static <T> String getFilelizeNameOfList(Map<String, T> objects) {
        var filelizeNameOptional = objects.values().stream()
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
            return clazz.getSimpleName();
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
        return calculateMD5(object);
    }

    public static String getFilelizeDirectory(Object obj) {
        var clazz = getClazz(obj);
        var filelizeAnnotation = clazz.getAnnotation(Filelize.class);
        if (filelizeAnnotation != null) {
            return filelizeAnnotation.directory();
        } else {
            return "";
        }
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

    private static String calculateMD5(Object obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();
            byte[] bytes = baos.toByteArray();

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(bytes);

            StringBuilder result = new StringBuilder();
            for (byte b : digest) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
