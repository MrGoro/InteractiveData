package de.schuermann.interactivedata.spring.data;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.data.DataSource;
import de.schuermann.interactivedata.api.filter.Filter;
import de.schuermann.interactivedata.api.filter.FilterData;
import de.schuermann.interactivedata.api.functions.Function;
import de.schuermann.interactivedata.api.granularity.Granularity;
import de.schuermann.interactivedata.spring.data.processors.FilterProcessor;
import de.schuermann.interactivedata.spring.service.ReflectionService;
import de.schuermann.interactivedata.spring.util.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Basic Implementation of a DataSource using Spring Data Repositories and Processors.
 *
 * @author Philipp Schürmann
 */
public abstract class JpaSpecificationDataSource<T> implements DataSource {

    @Autowired
    private ReflectionService reflectionService;

    @Override
    public ChartData getData(AbstractChartDefinition chartDefinition, List<Filter> filters) {
        JpaSpecificationExecutor<T> repository = getRepository();
        List<T> dbResult = repository.findAll(getSpecification(chartDefinition, filters));
        return postProcess(dbResult, chartDefinition, filters);
    }

    protected abstract JpaSpecificationExecutor<T> getRepository();

    private Specification<T> getSpecification(AbstractChartDefinition chartDefinition, List<Filter> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for(Filter filter : filters) {
                FilterProcessor filterProcessor = reflectionService.getFilterProcessor(filter.getClass());
                predicates.add(filterProcessor.filter(root, query, cb, filter));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    private ChartData postProcess(List<T> dbResult, AbstractChartDefinition chartDefinition, List<Filter> filters) {
        return null;
    }

}
