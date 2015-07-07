package de.schuermann.interactivedata.spring.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Philipp Schürmann
 */
public class ReflectionUtil {

    public static List<Class<?>> findClasses(String packagePath, List<TypeFilter> includeFilters) {
        List<Class<?>> annotated = new ArrayList<>();

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        includeFilters.forEach(scanner::addIncludeFilter);

        for (BeanDefinition bd : scanner.findCandidateComponents(packagePath)){
            String className = bd.getBeanClassName();
            Class<?> type = ClassUtils.resolveClassName(className, null);
            annotated.add(type);
        }

        return annotated;
    }

    public static List<Class<?>> findAnnotatedClasses(String packagePath, final Class<? extends Annotation> annotationType) {
        List<TypeFilter> filters = new ArrayList<>();
        filters.add(new AnnotationTypeFilter(annotationType));

        return findClasses(packagePath, filters);
    }

    public static List<Class<?>> findAssignableClasses(String packagePath, final Class<?> assignableType) {
        List<TypeFilter> filters = new ArrayList<>();
        filters.add(new AssignableTypeFilter(assignableType));

        return findClasses(packagePath, filters);
    }

    public static List<Method> findAnnotatedMethods(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<>();

        Class<?> clazz = type;
        while (clazz != Object.class) { // need to iterated thought hierarchy in order to retrieve methods from above the current instance
            // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
            final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(clazz.getDeclaredMethods()));
            for (final Method method : allMethods) {
                if (annotation == null || method.isAnnotationPresent(annotation)) {
                    methods.add(method);
                }
            }
            // move to the upper class in the hierarchy in search for more methods
            clazz = clazz.getSuperclass();
        }

        return methods;
    }
}
