package de.schuermann.interactivedata.spring.util;

import de.schuermann.interactivedata.api.chart.processors.AnnotationProcessor;
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

    /**
     * Returns a List of Classes that implement the {@Link AnnotationsProcessor} Interface
     *
     * @param chartAnnotation Annotation to find the Processor for
     * @param path Path to the package to search in
     * @return Classes implementing @Link AnnotationsProcessor} Interface
     */
    public static Class<? extends AnnotationProcessor> getAnnotationProcessor(Annotation chartAnnotation, String path) {
        List<Class<? extends AnnotationProcessor>> annotationProcessors = new ArrayList<>();
        List<Class<?>> foundClasses = ReflectionUtil.findAssignableClasses(path, AnnotationProcessor.class);
        for(Class<?> clazz : foundClasses) {
            Class<? extends AnnotationProcessor> processor = (Class<? extends AnnotationProcessor>) clazz;
            annotationProcessors.add(processor);
        }
        for (Class<? extends AnnotationProcessor> processorClass : annotationProcessors) {
            Type[] interfaceTypes = processorClass.getGenericInterfaces();
            for (Type interfaceType : interfaceTypes) {
                ParameterizedType parameterizedType = (ParameterizedType) interfaceType;
                for(Type genericType : parameterizedType.getActualTypeArguments()) {
                    if (genericType == chartAnnotation.annotationType()) {
                        return processorClass;
                    }
                }
            }
        }
        return null;
    }
}
