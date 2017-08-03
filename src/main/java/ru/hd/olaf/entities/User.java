package ru.hd.olaf.entities;

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
    private Set<Topic> commentedTopics;     //топики, где пользователь оставил комментарии

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
    //@JsonBackReference
    public Set<Topic> getWrittenTopics() {
        return writtenTopics;
    }

    public void setWrittenTopics(Set<Topic> writtenTopics) {
        this.writtenTopics = writtenTopics;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_topic", catalog = "parsed_site", joinColumns = {
            @JoinColumn(name = "USER_ID", nullable = false, updatable = true)},
            inverseJoinColumns = {@JoinColumn(name = "TOPIC_ID",
                    nullable = false, updatable = true)})
    public Set<Topic> getCommentedTopics() {
        return commentedTopics;
    }

    public void setCommentedTopics(Set<Topic> commentedTopics) {
        this.commentedTopics = commentedTopics;
    }
}
