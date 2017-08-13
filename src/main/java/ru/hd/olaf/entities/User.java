package ru.hd.olaf.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by d.v.hozyashev on 31.07.2017.
 */
@Entity
@Table(name = "users", catalog = "parsed_site")
public class User {
    private Integer id;                     //первичный ключ
    private String username;                //имя пользователя
    private Integer rating;                 //рейтинг пользователя
    private String profile;                 //Путь до профиля
    private Set<Topic> writtenTopics = new HashSet<Topic>();    //топики, где пользователь автор
    private Set<Comment> comments = new HashSet<Comment>();

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String profile) {
        this.username = username;
        this.profile = profile;
    }

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username", nullable = false, length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "profile", length = 255)
    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Basic
    @Column(name = "rating")
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = false)
    //@JsonBackReference
    public Set<Topic> getWrittenTopics() {
        return writtenTopics;
    }

    public void setWrittenTopics(Set<Topic> writtenTopics) {
        this.writtenTopics = writtenTopics;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
    //@JsonBackReference
    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        return username != null ? username.equals(user.username) : user.username == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + (username != null ? username : "") + '\'' +
                ", profile='" + (profile != null ? profile : "") + '\'' +
                /*", writtenTopics=" + (writtenTopics != null ? writtenTopics : "") +
                ", comments=" + (username != null ? username : "") +*/
                '}';
    }
}
