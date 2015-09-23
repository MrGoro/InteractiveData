package com.github.mrgoro.interactivedata.spring.sample.datasource;

import com.github.mrgoro.interactivedata.api.data.bean.DataObject;
import com.github.mrgoro.interactivedata.api.data.source.FileDataSource;

import java.nio.file.Path;

/**
 * Sample implementation to demonstrate the use of {@link FileDataSource} to access CSV files.
 * <p>
 * As specified in {@link #getPath()} the data source uses a file with the name data.csv. The file has to be located
 * either on the classpath or in the users home directory.
 * <p>
 * As specified in {@link #getSeparator()} the data values have to be separated by commas. Default is semicolon.
 * <p>
 * While the method {@link #getMappings()} is not overridden the mapping of the values relates to the columns
 * names specified in the fist line one the file. This also sets the {@link #skipLines()} return value to 1 to
 * ignore the headers as values.
 * <p>
 * The file has the following format:
 * <pre>
 *     first_name,last_name,email,time
 *     Edward,Gonzales,egonzales0@nih.gov,2013-04-02T04:54:19
 *     Judith,Murphy,jmurphy1@freewebs.com,2015-02-01T17:46:46
 *     Victor,Jackson,vjackson2@answers.com,2015-07-26T20:28:14
 * </pre>
 * This results in {@link DataObject DataObjects} representing
 * classes of the following format:
 * <pre>
 * {@code
 *  class User {
 *      private String first_name;
 *      private String last_name;
 *      private String email;
 *      private String time;
 *
 *      public void setFirst_Name(String first_name) {
 *          this.first_name = first_name;
 *      }
 *
 *      public String getFirst_Name() {
 *          return this.first_name;
 *      }
 *
 *      // other getters and setters, empty constructor
 *  }
 * }
 * </pre>
 *
 * @author Philipp Sch&uuml;rmann
 */
public class UserCsvDataSource extends FileDataSource {

    @Override
    protected Path getPath() {
        return FileDataSource.getPathFromFromFilename("data.csv");
    }

    @Override
    protected String getSeparator() {
        return ",";
    }
}