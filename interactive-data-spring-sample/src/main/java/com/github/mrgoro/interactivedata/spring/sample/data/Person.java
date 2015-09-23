package com.github.mrgoro.interactivedata.spring.sample.data;

import javax.persistence.*;
import java.util.List;

/**
 * Data Entity for Users
 *
 * @author Philipp Sch&uuml;rmann
 */
@Entity
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;

    @OneToMany
    private List<Action> actions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
