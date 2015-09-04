package de.schuermann.interactivedata.api.service.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.ToLongFunction;

/**
 * Makes an Class return an long Value for comparison by calling a method.
 *
 * @param <A> Class that should be able to convert to long
 */
public class AnnotationToLongFunction<A extends Annotation> implements ToLongFunction<Class<?>> {

    private Class<A> annotationClass;
    private Method method;

    /**
     * Create a new ToLongFunction that calls the specified function on an object.
     *
     * @param annotationClass Class of the object
     * @param methodName Method name to call
     * @throws IllegalArgumentException When unable to access method
     */
    public AnnotationToLongFunction(Class<A> annotationClass, String methodName) throws IllegalArgumentException {
        this.annotationClass = annotationClass;
        try {
            Method longMethod = annotationClass.getMethod(methodName);
            if(longMethod != null) {
                method = longMethod;
            } else {
                throw new IllegalArgumentException("Annotation does not have specified Method " + methodName);
            }
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Annotation does not have specified Method " + methodName, e);
        }
    }

    @Override
    public long applyAsLong(Class<?> filterServiceClass) throws IllegalArgumentException {
        A anAnnotation = filterServiceClass.getAnnotation(annotationClass);
        try {
            Object longObject = method.invoke(anAnnotation);
            if(longObject instanceof Long) {
                return (long) longObject;
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot call method to transform to Long", e);
        }
        throw new IllegalArgumentException("Cannot get Long Value from Object");
    }
}