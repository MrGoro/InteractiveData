package com.github.mrgoro.interactivedata.api.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Utility for various reflective tasks.
 *
 * @author Philipp Sch&uuml;rmann
 */
public class ReflectionUtil {

    /**
     * Search the given Class for Methods annotated with the given annotation.
     *
     * Searches the entire extension hierarchy.
     *
     * @param type Class to search in
     * @param annotation Annotation to search for
     * @return List of Methods annotated with the given annotation
     */
    public static List<Method> findAnnotatedMethods(Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<>();
        Class<?> clazz = type;
        while (clazz != Object.class) { // need to iterated thought hierarchy in order to retrieve methods from above the current instance
            // iterate though the list of methods declared in the class represented by class variable, and add those annotated with the specified annotation
            final List<Method> allMethods = new ArrayList<>(Arrays.asList(clazz.getDeclaredMethods()));
            methods.addAll(allMethods
                    .stream()
                    .filter(
                            method -> annotation == null || method.isAnnotationPresent(annotation)
                    )
                    .collect(Collectors.toList()));
            // move to the upper class in the hierarchy in search for more methods
            clazz = clazz.getSuperclass();
        }

        return methods;
    }

    /**
     * Check if Class is of generic type {@code Class1<Class2>}.
     *
     * @param test Class to test
     * @param clazz1 Class1
     * @param interfacesArray Interfaces or
     * @return true if it is a generic implementation, false if not
     */
    public static boolean isGenericImplementation(Class<?> test, Class<?> clazz1, Class<?>... interfacesArray) {
        List<Class<?>> interfaces = Arrays.asList(interfacesArray);
        if(clazz1.isAssignableFrom(test)) {
            Class<?> checkingClass = test;
            while(checkingClass != Object.class && checkingClass != null) {
                Type[] interfaceTypes = test.getGenericInterfaces();

                AtomicLong parameterizedCount = new AtomicLong(0);
                Arrays.asList(interfaceTypes)
                        .forEach(interfaceType ->
                                parameterizedCount.addAndGet(interfaces.stream()
                                    .filter(anInterface -> checkParametrizedType(interfaceType, anInterface))
                                    .count()));

                if(parameterizedCount.get() == interfaces.size()) {
                    return true;
                }

                if(interfaces.size() == 1) {
                    Type genericSuperclass = test.getGenericSuperclass();
                    if (checkParametrizedType(genericSuperclass, interfaces.get(0))) {
                        return true;
                    }
                }
                checkingClass = checkingClass.getSuperclass();
            }
        }
        return false;
    }

    /**
     * Check if the Type is parameterized and has at least one of its Generic Interfaces is the specified Class
     *
     * @param interfaceType  Type to search in ({@link ParameterizedType})
     * @param clazz Class to check for
     * @return true if at least one generic interface is the class specified
     */
    public static boolean checkParametrizedType(Type interfaceType, Class clazz) {
        if(interfaceType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) interfaceType;
            for (Type genericType : parameterizedType.getActualTypeArguments()) {
                if (genericType == clazz) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Find the Generic Type of the form {@code Class<GenericType>}.
     *
     * @param clazz Class
     * @param index Index of the Generic Type
     * @throws IllegalArgumentException when the Class does not have a Generic Type at the specified position or has none.
     * @return Generic Type
     */
    public static Type getGenericType(Class clazz, int index) throws IllegalArgumentException {
        ParameterizedType type = (ParameterizedType) clazz.getGenericSuperclass();
        if(type.getActualTypeArguments() == null || type.getActualTypeArguments().length <= index) {
            throw new IllegalArgumentException("No generic type at the specified position");
        }
        return type.getActualTypeArguments()[index];
    }

    public static Class<?> getGenericClass(Class clazz, int index) throws IllegalArgumentException {
        Type type = getGenericType(clazz, index);
        if(type instanceof Class<?>) {
            return (Class<?>) type;
        }
        throw new IllegalArgumentException("Generic parameter is not of type Class");
    }

    /**
     * Helper method to provide an easy access to zero argument constructor instantiation.
     *
     * @param objectClass Class of the object
     * @param <T> Type of the object / class
     * @return Instance of the object class
     * @throws IllegalArgumentException when class does not have a default constructor, instantiation fails or class object is null
     */
    public static <T> T getInstance(Class<T> objectClass) throws IllegalArgumentException {
        try {
            if(objectClass != null) {
                return objectClass.newInstance();
            } else {
                throw new IllegalArgumentException("Cannot instantiate object of null class.");
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new IllegalArgumentException("Initializing Class [" + objectClass.getName() + "] failed.", e);
        }
    }

}
