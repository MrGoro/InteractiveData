package de.schuermann.interactivedata.api.data;

import de.schuermann.interactivedata.api.data.reflection.DataObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * DataSource that uses a data file with separated values in each line.
 *
 * Generates {@Link DataObject DataObjects} for each line. Mapping is used to associate values to their property names.
 *
 * To use the DataSource extend this class and override the abstract methods getPath() and getMapping().
 * See the method description for details on their implementation.
 *
 * To change the separator (default is ; ) override the method getSeparator().
 *
 * @author Philipp Schürmann
 */
public abstract class FileDataSource extends StreamDataSource {

    private static Log log = LogFactory.getLog(FileDataSource.class);

    /**
     * Specifies the {@Link Path} of the data file.
     *
     * Example:
     * <code>
     *     @Override
     *     protected Path getPath() {
     *         return FileSystem.getPath("folder", "data.csv");
     *     }
     * </code>
     *
     * @see FileSystem#getPath(String, String...)
     *
     * @return Path to the data file
     */
    protected abstract Path getPath();

    /**
     * Specifies the Mapping of columns inside the data file.
     *
     * The Mapping maps columns to propertys in the data objects.
     *
     * Example:
     * <code>
     *     @Override
     *     protected String[] getMapping() {
     *         return { "id", "name", "age" };
     *     }
     * </code>
     * With the example mapping objects created form the data source have the properties id, name and age.
     * Each line represents an object. Values in the first column are associated to the property id, values
     * in the second column to the property name and values in the third column to the property age.
     *
     * @return Mapping of columns inside the data file as an String Array
     */
    protected abstract String[] getMappings();

    /**
     * Specifies the separator used to split the line into parts of values.
     *
     * Default is semicolon. Override the method to change the separator.
     *
     * @return Separator to split each line
     */
    protected String getSeparator() {
        return ";";
    }

    /**
     * Convert a textual line into a {@Link DataObject} using the specified mapping and separator.
     *
     * @param line Line to convert
     * @return DataObject with values of the line
     */
    protected DataObject convertLine(String line) {
        DataObject dataObject = DataObject.createEmpty();
        String[] mappings = getMappings();
        String[] parts = line.split(getSeparator());
        for(int i=0; i < parts.length; i++) {
            if(mappings.length > i && parts.length > i) {
                dataObject.setProperty(mappings[i], parts[i]);
            }
        }
        return dataObject;
    }

    @Override
    protected List<DataObject> getData() {
        List<DataObject> data = new ArrayList<>();
        try {
            Files.lines(getPath()).forEach(
                line -> {
                    data.add((convertLine(line)));
                }
            );
        } catch (IOException e) {
            log.error("Could not read file: path[" + getPath() + "], returning zero data");
        }
        return data;
    }
}
