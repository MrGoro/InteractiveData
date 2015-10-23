package com.github.mrgoro.interactivedata.api.data.operations.functions;

import com.github.mrgoro.interactivedata.api.data.bean.DataObject;
import com.github.mrgoro.interactivedata.api.data.operations.EmptyOperationData;
import com.github.mrgoro.interactivedata.api.data.operations.OperationData;

import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;

/**
 * Function that concatenates the objects that are aggregated.
 * <p>
 * Concatenation produces a String with values separated by the separator specified in options (default is comma).
 * Uses {@code toString()} method of each object for string conversion.
 *
 * <blockquote>
 *     <b>Request Data:</b><br>
 *     none
 * </blockquote>
 * <blockquote>
 *     <b>Options:</b><br>
 *     separator - default ,
 * </blockquote>
 *
 * @see Collect
 * @author Philipp Sch&uuml;rmann
 */
public class Concatenation extends Function<EmptyOperationData, Concatenation.ConcatenationOptions> {

    public Concatenation(String fieldName, Class fieldClass, EmptyOperationData requestData, ConcatenationOptions options) {
        super(fieldName, fieldClass, requestData, options);
    }

    @Override
    public Collector<DataObject, ?, ?> toCollector() {
        return mapping(
                dataObject -> getProperty(dataObject).toString(),
                Collectors.joining(getOptions().getSeparator())
        );
    }

    /**
     * Specifications of options for {@link Concatenation}.
     *
     * @see Concatenation
     */
    public static class ConcatenationOptions implements OperationData {

        private String separator = ", ";

        public ConcatenationOptions() {
        }

        public String getSeparator() {
            return separator;
        }

        public void setSeparator(String separator) {
            this.separator = separator;
        }

        @Override
        public boolean hasData() {
            return true;
        }
    }
}