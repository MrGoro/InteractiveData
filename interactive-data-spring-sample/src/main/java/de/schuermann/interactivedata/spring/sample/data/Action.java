package de.schuermann.interactivedata.spring.sample.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Data Entity for Actions (Counts)
 *
 * @author Philipp Schürmann
 */
@Entity
public class Action {

    @Id
    @GeneratedValue
    private Long id;

    private Date time;

    @ManyToOne
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
