package de.schuermann.interactivedata.spring.data;

import de.schuermann.interactivedata.api.chart.data.ChartData;
import de.schuermann.interactivedata.api.chart.data.Variate;
import de.schuermann.interactivedata.api.chart.definitions.AbstractChartDefinition;
import de.schuermann.interactivedata.api.data.DataSource;
import de.schuermann.interactivedata.api.data.operations.filter.Filter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Basic Implementation of a DataSource using Spring Data Repositories and Processors.
 *
 * @author Philipp Schürmann
 */
public abstract class JpaSpecificationDataSource<T> implements DataSource {

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
/*                FilterProcessor filterProcessor = processorService.getFilterProcessor(filter.getClass());
                predicates.add(filterProcessor.filter(root, query, cb, filter));*/
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    private ChartData postProcess(List<T> dbResult, AbstractChartDefinition chartDefinition, List<Filter> filters) {
        ChartData chartData = new ChartData(chartDefinition.getName());

        List<T> filterResult = dbResult;
        for(Filter  filter : filters) {
            filterResult = filter(filterResult, filter);
        }

        chartData.addVariate(new Variate("", (List<Object>) filterResult));
        return chartData;
    }

    private List<T> filter(List<T> data, Filter filter) {
        return (List<T>) data.stream().filter(filter.toPredicate()).collect(toList());
    }

}
