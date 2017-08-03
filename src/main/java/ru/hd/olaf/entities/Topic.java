package ru.hd.olaf.entities;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by d.v.hozyashev on 31.07.2017.
 */
@Entity
@Table(name = "topics", schema = "parsed_site")
public class Topic {
    private Integer id;                 //первичный ключ
    private String URL;                 //URL топика
    private String title;               //тема топика
    private User author;                //автор
    private Set<User> commentators;     //комментаторы

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
    @Column(name = "url", nullable = false, length = 255)
    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ManyToOne
    @JoinColumn(name = "author")
    //@JsonBackReference
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_topic", catalog = "parsed_site", joinColumns = {
            @JoinColumn(name = "TOPIC_ID", nullable = false, updatable = true)},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID",
                    nullable = false, updatable = true)})
    public Set<User> getCommentators() {
        return commentators;
    }

    public void setCommentators(Set<User> commentators) {
        this.commentators = commentators;
    }
}
