package nl.tudelft.sem.sem54.fridge.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true, length = 191)
    private String username;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private Fridge fridge;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, fridge);
    }

    public User() {

    }

    public User(String username) {
        this.username = username;
    }

    /**
     * Construct for User. Takes unique user id and unique username.
     *
     * @param username username of the user (unique).
     * @param fridge the fridge user is associated with.
     */
    public User(String username, Fridge fridge) {
        this.username = username;
        this.fridge = fridge;

    }

    public Fridge getFridge() {
        return fridge;
    }

    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}