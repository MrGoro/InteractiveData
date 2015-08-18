package de.schuermann.interactivedata.spring.sample;

import org.junit.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

/**
 * @author Philipp Schürmann
 */
public class StreamTest {

    @Test
    public void groupingMultipleTest() {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("Person One", 1, 18));
        persons.add(new Person("Person Two", 1, 20));
        persons.add(new Person("Person Three", 1, 30));
        persons.add(new Person("Person Four", 2, 30));
        persons.add(new Person("Person Five", 2, 29));
        persons.add(new Person("Person Six", 3, 18));

        List<BiFunction<Map<String, Integer>, Person, Map<String, Integer>>> listOfReducers = new ArrayList<>();

        listOfReducers.add((m, p) -> addMap(m, "Count", Optional.ofNullable(m.get("Count")).orElse(0) + 1));
        listOfReducers.add((m, p) -> addMap(m, "Sum", Optional.ofNullable(m.get("Sum")).orElse(0) + 1));

        BiFunction<Map<String, Integer>, Person, Map<String, Integer>> applyList
                = (mapin, p) -> {
            Map<String, Integer> mapout = mapin;
            for (BiFunction<Map<String, Integer>, Person, Map<String, Integer>> f : listOfReducers) {
                mapout = f.apply(mapout, p);
            }
            return mapout;
        };
        BinaryOperator<Map<String, Integer>> combineMaps
                = (map1, map2) -> {
            Map<String, Integer> mapout = new HashMap<>();
            mapout.putAll(map1);
            mapout.putAll(map2);
            return mapout;
        };
        Map<String, Integer> map
                = persons
                .stream()
                .reduce(new HashMap<String, Integer>(),
                        applyList, combineMaps);
        System.out.println("map = " + map);

        map.get(1);
    }

    class Person {
        String name;
        int group;
        int age;

        public Person(String name, int group, int age) {
            this.name = name;
            this.group = group;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getGroup() {
            return group;
        }

        public int getAge() {
            return age;
        }
    }

    class Data {
        long average;
        long sum;

        public Data(long average, long sum) {
            this.average = average;
            this.sum = sum;
        }
    }

    public static <K, V> Map<K, V> addMap(Map<K, V> map, K k, V v) {
        Map<K, V> mapout = new HashMap<K, V>();
        mapout.putAll(map);
        mapout.put(k, v);
        return mapout;
    }
}
