package de.schuermann.interactivedata.spring.sample.data;

import javax.persistence.*;
import java.util.Date;

/**
 * Data Entity for Actions
 *
 * @author Philipp Sch&uuml;rmann
 */
@Entity
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "action_seq_gen")
    @SequenceGenerator(name = "action_seq_gen", sequenceName = "action_id_seq")
    private Long id;

    private Date time;

    @ManyToOne
    private Person person;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
