package nl.tudelft.sem.sem54.mainservice.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private float credits;

    public UserEntity(String username, float credits) {
        this.username = username;
        this.credits = credits;
    }

    public UserEntity(String username) {
        this.username = username;
        this.credits = 0;
    }

    public UserEntity() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getCredits() {
        return credits;
    }

    public void setCredits(float credits) {
        this.credits = credits;
    }

    public void addCredits(float diffCredits) {
        this.credits += diffCredits;
    }

    public boolean getFlagged() {
        return this.credits < -50.0f;
    }

    @Override
    public String toString() {
        return "UserEntity{"
                + "username='" + username + '\''
                + ", credits='" + credits + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserEntity that = (UserEntity) o;
        return this.username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}