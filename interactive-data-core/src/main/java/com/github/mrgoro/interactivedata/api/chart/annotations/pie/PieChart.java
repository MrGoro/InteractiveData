package com.github.mrgoro.interactivedata.api.chart.annotations.pie;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Philipp Sch&uuml;rmann
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PieChart {

    Field data();

    Field label()[] default {};
}
