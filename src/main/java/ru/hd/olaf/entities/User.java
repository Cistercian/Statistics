package ru.hd.olaf.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by d.v.hozyashev on 31.07.2017.
 */
@Entity
@Table(name = "users", catalog = "parsed_site")
public class User {
    private Integer id;                     //первичный ключ
    private String username;                //имя пользователя
    private Set<Topic> writtenTopics;       //топики, где пользователь автор
    private Set<Comment> comments;

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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author", cascade = CascadeType.REMOVE, orphanRemoval = false)
    @JsonBackReference
    public Set<Topic> getWrittenTopics() {
        return writtenTopics;
    }

    public void setWrittenTopics(Set<Topic> writtenTopics) {
        this.writtenTopics = writtenTopics;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = false)
    @JsonBackReference
    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
