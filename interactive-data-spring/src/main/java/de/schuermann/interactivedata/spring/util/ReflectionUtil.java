package de.schuermann.interactivedata.spring.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

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

    @Deprecated
    public static <D> Class<D> findGenericExtention(Class<D> clazz1, Class clazz2, String path) {
        List<Class<D>> foundClasses = ReflectionUtil.findAssignableClasses(path, clazz1);
        for (Class<D> processorClass : foundClasses) {
            Type interfaceType = processorClass.getGenericSuperclass();
            if(checkParametrizedType(interfaceType, clazz2)) {
                return processorClass;
            }
        }
        return null;
    }

    /**
     * Search the package path for classes matching the list of filters.
     *
     * @param path Package Path
     * @param includeFilters List of Filters to search for
     * @return List of Classes matching the filters
     */
    public static List<Class<?>> findClasses(String path, List<TypeFilter> includeFilters) {
        List<Class<?>> annotated = new ArrayList<>();

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        includeFilters.forEach(scanner::addIncludeFilter);

        for (BeanDefinition bd : scanner.findCandidateComponents(path)){
            String className = bd.getBeanClassName();
            Class<?> type = ClassUtils.resolveClassName(className, null);
            annotated.add(type);
        }

        return annotated;
    }

    /**
     * Search the package paht for Classes that are assignable to the given class type.
     *
     * @param path Package Path
     * @param assignableType Class Type
     * @param <D> Class Type
     * @return List of Classes that are assignable to the given class type
     */
    @SuppressWarnings("unchecked")
    public static <D> List<Class<D>> findAssignableClasses(String path, Class<D> assignableType) {
        List<TypeFilter> filters = new ArrayList<>();
        filters.add(new AssignableTypeFilter(assignableType));
        List<Class<?>> classes = findClasses(path, filters);

        List<Class<D>> assignableClasses = new ArrayList<>();
        classes.forEach(aClass -> assignableClasses.add((Class<D>) aClass));
        return assignableClasses;
    }

    /**
     * Get a List of Classes that are annotated with the given annotation.
     *
     * @param packagePath Package path to search in
     * @param annotationType Type of the Annotation to search for
     * @return List of Classes annotated with the given annotation
     */
    public static List<Class<?>> findAnnotatedClasses(String packagePath, final Class<? extends Annotation> annotationType) {
        List<TypeFilter> filters = new ArrayList<>();
        filters.add(new AnnotationTypeFilter(annotationType));

        return findClasses(packagePath, filters);
    }

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
     * Find Classes that Implement the generic interface of form <code>Class1<Class2></code>
     *
     * @param clazz1 Base Class
     * @param clazz2 Generic Type Parameter
     * @param path Package Path
     * @param <D> Base Class Type
     * @return Class that implements the generic Interface <code>Class1<Class2></code>
     */
    public static <D> Class<? extends D> getGenericImplementation(Class<D> clazz1, Class clazz2, String path) {
        List<Class<D>> foundClasses = ReflectionUtil.findAssignableClasses(path, clazz1);
        for (final Class<D> processorClass : foundClasses) {
            // Also check if Interfaces implemented in super class
            Class<?> checkingClass = processorClass;
            while(checkingClass != Object.class) {
                Type[] interfaceTypes = processorClass.getGenericInterfaces();
                for (Type interfaceType : interfaceTypes) {
                    if (checkParametrizedType(interfaceType, clazz2)) {
                        return processorClass;
                    }
                }
                Type genericSuperclass = processorClass.getGenericSuperclass();
                if (checkParametrizedType(genericSuperclass, clazz2)) {
                    return processorClass;
                }
                checkingClass = checkingClass.getSuperclass();
            }
        }
        return null;
    }

    /**
     * Check if the Type is parameterized and has at least one of its Generic Interfaces is the specified Class
     *
     * @param interfaceType  Type to search in ({@Link ParameterizedType})
     * @param clazz Class to check for
     * @return true if at least one generic interface is the class specified
     */
    private static boolean checkParametrizedType(Type interfaceType, Class clazz) {
        ParameterizedType parameterizedType = (ParameterizedType) interfaceType;
        for (Type genericType : parameterizedType.getActualTypeArguments()) {
            if (genericType == clazz) {
                return true;
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
     *
     * @param objectClass
     * @param <T>
     * @return
     * @throws IllegalArgumentException when class does not have a default constructor or instantiation fails
     */
    public static <T> T getInstance(Class<T> objectClass) throws IllegalArgumentException {
        try {
            return objectClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new IllegalArgumentException("Initializing Class [" + objectClass.getName() + "] failed.", e);
        }
    }

}
