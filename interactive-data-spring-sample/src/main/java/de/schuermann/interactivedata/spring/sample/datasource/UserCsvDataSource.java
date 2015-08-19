package de.schuermann.interactivedata.spring.sample.datasource;

import de.schuermann.interactivedata.api.data.FileDataSource;

import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Philipp Schürmann
 */
public class UserCsvDataSource extends FileDataSource {

    @Override
    protected Path getPath() {
        Path path = FileSystems.getDefault().getPath("data.csv");
        try {
            return Paths.get(getClass().getClassLoader()
                    .getResource("data.csv")
                    .toURI());
        } catch (URISyntaxException ignored) {}
        return null;
    }

    @Override
    protected String getSeparator() {
        return ",";
    }
}
