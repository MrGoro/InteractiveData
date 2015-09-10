package de.schuermann.interactivedata.api.data.operations.filter;

import de.schuermann.interactivedata.api.data.operations.OperationData;
import de.schuermann.interactivedata.api.data.bean.DataObject;
import de.schuermann.interactivedata.api.util.exceptions.ChartDefinitionException;

import java.util.OptionalDouble;

/**
 * @author Philipp Sch&uuml;rmann
 */
public class LocationFilter extends Filter<LocationFilter.LocationFilterData, LocationFilter.LocationOptions> {

    public LocationFilter(String fieldName, Class fieldClass, LocationFilterData requestData, LocationOptions options) {
        super(fieldName, fieldClass, requestData, options);
    }

    @Override
    protected boolean test(DataObject dataObject) {
        double coord;
        if(getFieldClass() == String.class) {
            coord = Double.parseDouble(dataObject.getOptionalProperty(getFieldName(), String.class).orElse("0.0"));
        } else if(getFieldClass() == Double.class || getFieldClass() == double.class) {
            coord = dataObject.getOptionalProperty(getFieldName(), double.class).orElse(0.0);
        } else {
            throw new ChartDefinitionException("Latitude or Longitude for LocationFilter must be of type double or String (parsable double)");
        }
        if(isLatitude()) {
            return checkLatitude(coord);
        } else {
            return checkLongitude(coord);
        }
    }

    private boolean isLatitude() {
        return getOptions().getType() == LocationOptions.Type.LATITUDE;
    }

    private boolean checkLatitude(double latitude) {
        return getRequestData().top() >= latitude && latitude >= getRequestData().bottom();
    }

    private boolean checkLongitude(double longitude) {
        return (getRequestData().right() <= getRequestData().right() && getRequestData().left() <= longitude && longitude <= getRequestData().right()) ||
                (getRequestData().left() > getRequestData().right() && (getRequestData().left() <= longitude || longitude <= getRequestData().right()));
    }

    public static class LocationOptions implements OperationData {

        enum Type {
            LATITUDE, LONGITUDE
        }

        private Type type;

        @Override
        public boolean hasData() {
            return type != null;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }
    }

    public static class LocationFilterData implements OperationData {

        // Use Java 8 Optionals to differentiate between 0.0 and absent
        private OptionalDouble top;
        private OptionalDouble right;
        private OptionalDouble bottom;
        private OptionalDouble left;

        @Override
        public boolean hasData() {
            return top.isPresent() &&
                    bottom.isPresent() ||
                    right.isPresent() &&
                            left.isPresent();
        }

        public OptionalDouble getTop() {
            return top;
        }

        public void setTop(OptionalDouble top) {
            this.top = top;
        }

        public OptionalDouble getRight() {
            return right;
        }

        public void setRight(OptionalDouble right) {
            this.right = right;
        }

        public OptionalDouble getBottom() {
            return bottom;
        }

        public void setBottom(OptionalDouble bottom) {
            this.bottom = bottom;
        }

        public OptionalDouble getLeft() {
            return left;
        }

        public void setLeft(OptionalDouble left) {
            this.left = left;
        }

        // Direct Double value access
        public double top() {
            return this.top.getAsDouble();
        }

        public double right() {
            return this.right.getAsDouble();
        }

        public double bottom() {
            return this.bottom.getAsDouble();
        }

        public double left() {
            return this.left.getAsDouble();
        }

        @Override
        public String toString() {
            return "LocationFilterData{" +
                    "top=" + top +
                    ", right=" + right +
                    ", bottom=" + bottom +
                    ", left=" + left +
                    '}';
        }
    }
}
