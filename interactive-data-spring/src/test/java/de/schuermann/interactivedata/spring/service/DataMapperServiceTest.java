package de.schuermann.interactivedata.spring.service;

import de.schuermann.interactivedata.spring.config.InteractiveDataTestConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Philipp Schürmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = InteractiveDataTestConfiguration.class)
public class DataMapperServiceTest {

    @Autowired
    private DataMapperService dataMapperService;

    private MultivaluedMap<String, String> multivaluedMap;

    public static final String NAME = "FirstName LastName";
    public static final String WEIGHT = "15.74";
    public static final String NUMBER = "34524";
    public static final String BIRTHDAY = "1436972483";
    public static final List<String> DATES = Arrays.asList("1436972383", "1436971183");

    @Before
    public void setUp() throws Exception {
        multivaluedMap = new MultivaluedHashMap<>();
        multivaluedMap.putSingle("name", NAME);
        multivaluedMap.putSingle("weight", WEIGHT);
        multivaluedMap.putSingle("weight", NUMBER);
        multivaluedMap.putSingle("weight", BIRTHDAY);
        multivaluedMap.put("weight", DATES);
    }

    @Test
    public void testValidParameters() throws Exception {
        AnimalData animalData = dataMapperService.mapDataOnObject(AnimalData.class, multivaluedMap);
        Assert.assertNotNull(animalData);
        Assert.assertEquals(NAME, animalData.getName());
        Assert.assertEquals(Float.parseFloat(WEIGHT), animalData.getWeight(), 0.0);
        Assert.assertEquals(Integer.parseInt(NUMBER), animalData.getNumber());
        Assert.assertEquals(Long.parseLong(BIRTHDAY), animalData.getBirthday().getTime());
        for(Date date : animalData.getDates()) {
            Assert.assertTrue(DATES.contains(String.valueOf(date.getTime())));
        }
    }

    public class AnimalData {

        private String name;
        private float weight;
        private int number;
        private Date birthday;
        private List<Date> dates;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getWeight() {
            return weight;
        }

        public void setWeight(float weight) {
            this.weight = weight;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }

        public List<Date> getDates() {
            return dates;
        }

        public void setDates(List<Date> dates) {
            this.dates = dates;
        }
    }

}
