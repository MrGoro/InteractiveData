package de.schuermann.interactivedata.api.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility for various reflection tasks.
 *
 * @author Philipp Schürmann
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
     * Check if Class is of generic type <code>Class1<Class2></code>
     *
     * @param clazz1 Class1
     * @param clazz2 Class2
     * @return boolean
     */
    public static boolean isGenericImplementation(Class<?> test, Class<?> clazz1, Class<?> clazz2) {
        if(clazz1.isAssignableFrom(test)) {
            Class<?> checkingClass = test;
            while(checkingClass != Object.class) {
                Type[] interfaceTypes = test.getGenericInterfaces();
                for (Type interfaceType : interfaceTypes) {
                    if (checkParametrizedType(interfaceType, clazz2)) {
                        return true;
                    }
                }
                Type genericSuperclass = test.getGenericSuperclass();
                if (checkParametrizedType(genericSuperclass, clazz2)) {
                    return true;
                }
                checkingClass = checkingClass.getSuperclass();
            }
        }
        return false;
    }

    /**
     * Check if the Type is parameterized and has at least one of its Generic Interfaces is the specified Class
     *
     * @param interfaceType  Type to search in ({@Link ParameterizedType})
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
     * Find the Generic Type of the form Class<GenericType>.
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
