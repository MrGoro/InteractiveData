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
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * DataSource that uses a data file with separated values in each line.
 *
 * Generates {@link DataObject DataObjects} for each line. Mapping is used to associate values to their property names.
 *
 * To use the DataSource you have to extend this abstract class and override at least the method {@link #getPath()} to
 * specify the path of your file you want to use.
 *
 * A mapper is used to convert each line into a {@link DataObject}. The default mapping is derived from the first line
 * of the file. The default separator to split the values of each line is ;. To adjust this behaviour override the methods
 * {@link #getMappings()} and {@link #getSeparator()}.
 *
 * @author Philipp Sch&uuml;rmann
 */
public abstract class FileDataSource extends StreamDataSource<String> {

    private static Log log = LogFactory.getLog(FileDataSource.class);

    /**
     * Specifies the {@link Path} of the data file.
     *
     * Example:
     * <pre>
     * {@code
     *      {@literal @}Override
     *      protected Path getPath() {
     *          FileSystem filesystem = ...
     *          return filesystem.getPath("folder", "data.csv");
     *      }
     * }
     * </pre>
     *
     * @see FileSystem#getPath(String, String...)
     *
     * @return Path to the data file
     */
    protected abstract Path getPath();

    /**
     * Specifies the Mapping of columns inside the data file. The Mapping maps columns to properties in the data objects.
     *
     * The default mapping is derived from the first line of the file. The separator from {@link #getSeparator()} is used.
     *
     * Example:
     * <pre>
     * {@code
     *      {@literal @}Override
     *      protected String[] getMapping() {
     *          return { "id", "name", "age" };
     *      }
     * }
     * </pre>
     *
     * With the example mapping objects created form the data source have the properties id, name and age.
     * Each line represents an object. Values in the first column are associated to the property id, values
     * in the second column to the property name and values in the third column to the property age.
     *
     * When overriding the method note to adjust (override) the method {@link #skipLines()} so that the first line
     * is not skipped.
     *
     * @return Mapping of columns inside the data file as an String Array
     */
    protected String[] getMappings() {
        try {
            List<String[]> list = Files.lines(getPath()).limit(1).map(
                    line -> line.split(getSeparator())
            ).collect(toList());
            if(list != null && list.size() == 1) {
                return list.get(0);
            }
        } catch (IOException e) {
            log.error("Could not read file: path[" + getPath() + "], returning zero data");
        }
        throw new IllegalArgumentException("Could not get Mapping for FileDataSource of file[" + getPath().getFileName() + "]");
    }

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
     * Specifies the lines to skip.
     *
     * Useful when files like csv define a header with the mapping.
     *
     * @see #getMappings()
     * @return Lines to skip
     */
    protected long skipLines() {
        return 1;
    }

    @Override
    protected Stream<String> getDataStream() {
        try {
            return Files.lines(getPath()).skip(skipLines());
        } catch (IOException e) {
            log.error("Could not read file: path[" + getPath() + "], returning zero data");
        }
        return new ArrayList<String>().stream();
    }

    @Override
    protected Function<String, DataObject> getMapper() {
        return this::convertLine;
    }

    /**
     * Convert a textual line into a {@link DataObject} using the specified mapping and separator.
     *
     * @see #getMapper()
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
}
