package de.schuermann.interactivedata.processors;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Philipp Schürmann
 */
public class ServiceClassLocator {

    public static final String ANNOTATED_RESOURCE = "META-INF/annotations/";

    public static Iterable<Class<?>> getAnnotated(Class<? extends Annotation> annotation) {
        return getAnnotated(annotation, Thread.currentThread().getContextClassLoader());
    }

    public static Iterable<Class<?>> getAnnotated(Class<? extends Annotation> annotation, ClassLoader classLoader) {
        Iterable<String> entries = getAnnotatedNames(annotation, classLoader);
        Set<Class<?>> classes = new HashSet<>();
        findClasses(classLoader, classes, entries);
        return classes;
    }

    public static Iterable<String> getAnnotatedNames(Class<? extends Annotation> annotation) {
        return getAnnotatedNames(annotation, Thread.currentThread().getContextClassLoader());
    }

    public static Iterable<String> getAnnotatedNames(Class<? extends Annotation> annotation, ClassLoader classLoader) {
        return readIndexFile(classLoader, ANNOTATED_RESOURCE + annotation.getCanonicalName());
    }

    private static Iterable<String> readIndexFile(ClassLoader classLoader, String resourceFile) {
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
                } catch (FileNotFoundException e) {}
            }
        } catch (IOException e) {
            throw new RuntimeException("ClassIndex: Cannot read class index", e);
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
