package de.schuermann.interactivedata.api.service;

import de.schuermann.interactivedata.api.filter.Filter;

import java.util.Collection;

/**
 * @author Philipp Schürmann
 */
public interface ServiceLocator {

    Collection<Class<? extends Filter>> getFilterClasses();

    Collection<Class<?>> getChartServices();
}
