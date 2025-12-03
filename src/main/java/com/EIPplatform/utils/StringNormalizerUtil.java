package com.EIPplatform.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StringNormalizerUtil {

    private static final Map<Class<?>, Field[]> FIELD_CACHE = new ConcurrentHashMap<>();

    private static final ThreadLocal<Set<Integer>> PROCESSED_OBJECTS = ThreadLocal.withInitial(HashSet::new);

    /**
     * Normalize tất cả các trường String trong object
     * 
     * @param request - Object cần normalize (DTO)
     * @return Object đã được normalize
     */
    public static <T> T normalizeRequest(T request) {
        if (request == null) {
            return null;
        }

        try {
            PROCESSED_OBJECTS.get().clear();
            normalizeObject(request);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error normalizing request: " + e.getMessage(), e);
        } finally {
            PROCESSED_OBJECTS.remove();
        }

        return request;
    }

    private static void normalizeObject(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return;
        }

        int objectId = System.identityHashCode(obj);
        if (PROCESSED_OBJECTS.get().contains(objectId)) {
            return;
        }
        PROCESSED_OBJECTS.get().add(objectId);

        Class<?> clazz = obj.getClass();

        if (shouldSkipClass(clazz)) {
            return;
        }

        Field[] fields = getFieldsFromCache(clazz);

        for (Field field : fields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) ||
                    java.lang.reflect.Modifier.isTransient(field.getModifiers())) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(obj);

            if (value == null) {
                continue;
            }

            if (value instanceof String) {
                String normalizedValue = normalizeSpaces((String) value);
                field.set(obj, normalizedValue);
            } else if (value instanceof Collection) {
                normalizeCollection((Collection<?>) value);
            } else if (value instanceof Map) {
                normalizeMap((Map<?, ?>) value);
            } else if (value.getClass().isArray()) {
                normalizeArray(value);
            } else if (!isPrimitiveOrWrapper(value.getClass())) {
                normalizeObject(value);
            }
        }
    }

    /**
     * Normalize Collection - xử lý cả immutable collection
     */
    private static void normalizeCollection(Collection<?> collection) throws IllegalAccessException {
        if (collection == null || collection.isEmpty()) {
            return;
        }

        try {
            collection.getClass().getMethod("add", Object.class);
        } catch (NoSuchMethodException e) {
            for (Object item : collection) {
                if (item != null && !isPrimitiveOrWrapper(item.getClass())) {
                    normalizeObject(item);
                }
            }
            return;
        }

        for (Object item : collection) {
            if (item != null && !isPrimitiveOrWrapper(item.getClass())) {
                normalizeObject(item);
            }
        }
    }

    /**
     * Normalize Map - xử lý cả key và value nếu là String hoặc object
     */
    private static void normalizeMap(Map<?, ?> map) throws IllegalAccessException {
        if (map == null || map.isEmpty()) {
            return;
        }

        Map<Object, Object> normalizedEntries = new HashMap<>();
        boolean needsUpdate = false;

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            Object normalizedKey = key;
            if (key instanceof String) {
                normalizedKey = normalizeSpaces((String) key);
                if (!normalizedKey.equals(key)) {
                    needsUpdate = true;
                }
            }

            Object normalizedValue = value;
            if (value instanceof String) {
                normalizedValue = normalizeSpaces((String) value);
                if (!normalizedValue.equals(value)) {
                    needsUpdate = true;
                }
            } else if (value != null && !isPrimitiveOrWrapper(value.getClass())) {
                normalizeObject(value);
            }

            if (needsUpdate || !Objects.equals(key, normalizedKey) || !Objects.equals(value, normalizedValue)) {
                normalizedEntries.put(normalizedKey, normalizedValue);
            }
        }

        if (needsUpdate && !normalizedEntries.isEmpty()) {
            try {
                map.clear();
                ((Map<Object, Object>) map).putAll(normalizedEntries);
            } catch (UnsupportedOperationException e) {
            }
        }
    }

    /**
     * Normalize Array - xử lý cả primitive và object array
     */
    private static void normalizeArray(Object arrayObj) throws IllegalAccessException {
        if (arrayObj == null) {
            return;
        }

        Class<?> componentType = arrayObj.getClass().getComponentType();

        if (componentType.isPrimitive()) {
            return;
        }

        if (componentType == String.class) {
            String[] array = (String[]) arrayObj;
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                    array[i] = normalizeSpaces(array[i]);
                }
            }
            return;
        }
        int length = Array.getLength(arrayObj);
        for (int i = 0; i < length; i++) {
            Object item = Array.get(arrayObj, i);
            if (item != null && !isPrimitiveOrWrapper(item.getClass())) {
                normalizeObject(item);
            }
        }
    }

    /**
     * Hàm normalize spaces cho String
     */
    private static String normalizeSpaces(String input) {
        if (input == null) {
            return null;
        }
        return input.trim().replaceAll("\\s+", " ");
    }

    /**
     * Get fields từ cache hoặc reflection
     */
    private static Field[] getFieldsFromCache(Class<?> clazz) {
        return FIELD_CACHE.computeIfAbsent(clazz, k -> {
            List<Field> allFields = new ArrayList<>();
            Class<?> currentClass = clazz;

            while (currentClass != null && currentClass != Object.class) {
                Field[] fields = currentClass.getDeclaredFields();
                allFields.addAll(Arrays.asList(fields));
                currentClass = currentClass.getSuperclass();
            }

            return allFields.toArray(new Field[0]);
        });
    }

    /**
     * Kiểm tra có nên skip class này không
     */
    private static boolean shouldSkipClass(Class<?> clazz) {
        String className = clazz.getName();
        return clazz.isPrimitive() ||
                className.startsWith("java.") ||
                className.startsWith("javax.") ||
                className.startsWith("sun.") ||
                className.startsWith("jdk.") ||
                clazz.isEnum() ||
                clazz.isAnnotation();
    }

    /**
     * Kiểm tra xem class có phải là primitive hoặc wrapper không
     */
    private static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz.equals(String.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(Boolean.class) ||
                clazz.equals(Byte.class) ||
                clazz.equals(Short.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(java.util.Date.class) ||
                clazz.equals(java.time.LocalDate.class) ||
                clazz.equals(java.time.LocalDateTime.class) ||
                clazz.equals(java.time.ZonedDateTime.class) ||
                clazz.equals(java.time.Instant.class) ||
                clazz.equals(java.math.BigDecimal.class) ||
                clazz.equals(java.math.BigInteger.class) ||
                clazz.equals(java.util.UUID.class);
    }

    /**
     * Clear field cache (nếu cần trong testing hoặc hot reload)
     */
    public static void clearCache() {
        FIELD_CACHE.clear();
    }
}