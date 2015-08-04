package de.schuermann.interactivedata.processors;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementScanner6;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.util.*;

/**
 * @author Philipp Schuermann
 */
@SupportedAnnotationTypes(value= {"de.schuermann.interactivedata.api.service.*"})
public class ServiceAnnotationProcessor extends AbstractProcessor {

    private Map<String, Set<String>> annotatedServices = new HashMap<>();

    private Types types;
    private Filer filer;
    private Elements elementUtils;
    private Messager messager;

    public ServiceAnnotationProcessor() {

    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        types = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getRootElements()) {
            if (!(element instanceof TypeElement)) {
                continue;
            }
            element.accept(new ElementScanner6<Void, Void>() {
                @Override
                public Void visitType(TypeElement typeElement, Void o) {
                    for (AnnotationMirror mirror : typeElement.getAnnotationMirrors()) {
                        final TypeElement annotationElement = (TypeElement) mirror.getAnnotationType().asElement();
                        annotationElement.getQualifiedName();
                        storeAnnotation(annotationElement, typeElement);
                    }
                    return super.visitType(typeElement, o);
                }
            }, null);
        }

        if (!roundEnv.processingOver()) {
            return false;
        }

        writeToFiles();
        return false;
    }

    private void storeAnnotation(TypeElement annotationElement, TypeElement rootElement) {
        ProcessedAnnotation processedAnnotation = annotationElement.getAnnotation(ProcessedAnnotation.class);
        if(processedAnnotation != null) {
            addElement(annotatedServices, annotationElement.getSimpleName().toString(), rootElement.getQualifiedName().toString());
        }
    }

    private void addElement(Map<String, Set<String>> map, String key, String value) {
        if(map != null) {
            Set<String> set = map.get(key);
            if(set == null) {
                set = new HashSet<>();
                map.put(key, set);
            }
            set.add(value);
        }
    }

    private void writeToFiles() {
        try {
            for (Map.Entry<String, Set<String>> entry : annotatedServices.entrySet()) {
                messager.printMessage(Diagnostic.Kind.NOTE, "Writing " + entry.getValue().size() + " Services for [" + entry.getKey() + "]");
                writeSimpleNameIndexFile(entry.getValue(), ServiceClassLocator.ANNOTATED_RESOURCE + entry.getKey());
            }
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

    private void writeSimpleNameIndexFile(Set<String> elementList, String resourceName)
            throws IOException {
        readOldIndexFile(elementList, resourceName);
        writeIndexFile(elementList, resourceName);
    }

    private void readOldIndexFile(Set<String> entries, String resourceName) throws IOException {
        Reader reader = null;
        try {
            final FileObject resource = filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceName);
            reader = resource.openReader(true);
            readOldIndexFile(entries, reader);
        } catch (FileNotFoundException e) {
            /**
             * Ugly hack for Intellij IDEA incremental compilation.
             * The problem is that it throws FileNotFoundException on the files, if they were not created during the
             * current session of compilation.
             */
            final String realPath = e.getMessage();
            if (new File(realPath).exists()) {
                try (Reader fileReader = new FileReader(realPath)) {
                    readOldIndexFile(entries, fileReader);
                }
            }
        } catch (IOException | UnsupportedOperationException e) {
            messager.printMessage(Diagnostic.Kind.WARNING, e.getMessage());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private static void readOldIndexFile(Set<String> entries, Reader reader) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                entries.add(line);
                line = bufferedReader.readLine();
            }
        }
    }

    private void writeIndexFile(Set<String> entries, String resourceName) throws IOException {
        FileObject file = filer.createResource(StandardLocation.CLASS_OUTPUT, "", resourceName);
        try (Writer writer = file.openWriter()) {
            for (String entry : entries) {
                writer.write(entry);
                writer.write("\n");
            }
            writer.close();
        }
    }
}
