package com.github.mrgoro.interactivedata.spring.service.locators;

import com.github.mrgoro.interactivedata.api.util.ReflectionUtil;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Sch&uuml;rmann
 */
public class ClassLocatorUtil {

    /**
     * Search the package path for classes matching the list of filters.
     *
     * @param path Package Path
     * @param includeFilters List of Filters to search for
     * @return List of Classes matching the filters
     */
    static List<Class<?>> findClasses(String path, List<TypeFilter> includeFilters) {
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
     * Search the package path for Classes that are assignable to the given class type.
     *
     * @param path Package Path
     * @param assignableType Class Type
     * @param <D> Class Type
     * @return List of Classes that are assignable to the given class type
     */
    @SuppressWarnings("unchecked")
    @Cacheable("assignableClasses")
    static <D> List<Class<D>> findAssignableClasses(String path, Class<D> assignableType) {
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
    @Cacheable("annotatedClasses")
    static List<Class<?>> findAnnotatedClasses(String packagePath, final Class<? extends Annotation> annotationType) {
        List<TypeFilter> filters = new ArrayList<>();
        filters.add(new AnnotationTypeFilter(annotationType));

        return findClasses(packagePath, filters);
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
    @Deprecated
    static <D> Class<? extends D> getGenericImplementation(Class<D> clazz1, Class clazz2, String path) {
        List<Class<D>> foundClasses = findAssignableClasses(path, clazz1);
        for (final Class<D> processorClass : foundClasses) {
            // Also check if Interfaces implemented in super class
            Class<?> checkingClass = processorClass;
            while(checkingClass != Object.class) {
                Type[] interfaceTypes = processorClass.getGenericInterfaces();
                for (Type interfaceType : interfaceTypes) {
                    if (ReflectionUtil.checkParametrizedType(interfaceType, clazz2)) {
                        return processorClass;
                    }
                }
                Type genericSuperclass = processorClass.getGenericSuperclass();
                if (ReflectionUtil.checkParametrizedType(genericSuperclass, clazz2)) {
                    return processorClass;
                }
                checkingClass = checkingClass.getSuperclass();
            }
        }
        return null;
    }
}
