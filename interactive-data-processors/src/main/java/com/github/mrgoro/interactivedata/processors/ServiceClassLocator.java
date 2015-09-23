package com.github.mrgoro.interactivedata.processors;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * Locator for Service Classes. Uses index files from an annotation processor (JSR-268).
 *
 * @author Philipp Sch&uuml;rmann
 */
public class ServiceClassLocator {

    public static final String ANNOTATED_RESOURCE = "META-INF/annotations/";

    /**
     * Get a collection of all class definitions for classes annotated with the specified annotation.
     *
     * @param annotation Annotation to search for
     * @return Collection of classes
     */
    public static Collection<Class<?>> getAnnotated(Class<? extends Annotation> annotation) {
        return getAnnotated(annotation, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Get a collection of all class definitions for classes annotated with the specified annotation.
     *
     * Uses the specified class loader to load the class definition.
     *
     * @param annotation Annotation to search for
     * @param classLoader ClassLoader to use for loading the class definition
     * @return  Collection of classes
     */
    public static Collection<Class<?>> getAnnotated(Class<? extends Annotation> annotation, ClassLoader classLoader) {
        Iterable<String> entries = getAnnotatedNames(annotation, classLoader);
        Set<Class<?>> classes = new HashSet<>();
        findClasses(classLoader, classes, entries);
        return classes;
    }

    private static Collection<String> getAnnotatedNames(Class<? extends Annotation> annotation, ClassLoader classLoader) {
        return readIndexFile(classLoader, ANNOTATED_RESOURCE + annotation.getCanonicalName());
    }

    private static Collection<String> readIndexFile(ClassLoader classLoader, String resourceFile) {
        Set<String> entries = new HashSet<>();
        try {
            Enumeration<URL> resources = classLoader.getResources(resourceFile);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream(), "UTF-8"))) {

                    String line = reader.readLine();
                    while (line != null) {
                        entries.add(line);
                        line = reader.readLine();
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("Resource file not found [" + resourceFile + "]", e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading resource file [" + resourceFile + "]", e);
        }
        return entries;
    }

    private static void findClasses(ClassLoader classLoader, Set<Class<?>> classes, Iterable<String> entries) {
        for (String entry : entries) {
            Class<?> klass;
            try {
                klass = classLoader.loadClass(entry);
            } catch (ClassNotFoundException e) {
                continue;
            }
            classes.add(klass);
        }
    }
}
