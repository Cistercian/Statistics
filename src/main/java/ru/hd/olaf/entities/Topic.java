package ru.hd.olaf.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by d.v.hozyashev on 31.07.2017.
 */
@Entity
@Table(name = "topics", schema = "parsed_site")
public class Topic {
    private Integer id;                 //первичный ключ
    private String url;                 //URL топика
    private String title;               //тема топика
    private Integer rating;             //рейтинг
    private Integer topicId;            //id топика
    private User author;                //автор
    private Set<Comment> comments = new HashSet<Comment>();

    public Topic() {
    }

    public Topic(Integer topicId) {
        this.topicId = topicId;
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
    @Column(name = "url", nullable = false, length = 255)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "topic_id")
    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    @Basic
    @Column(name = "rating")
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @ManyToOne
    @JoinColumn(name = "author")
    @JsonBackReference
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic", cascade = CascadeType.REMOVE, orphanRemoval = false)
    @JsonBackReference
    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", url='" + (url != null ? url : "") + '\'' +
                ", title='" + (title != null ? title : "") + '\'' +
                ", topicId=" + (topicId != null ? topicId : "n/a") +
                ", author=" + (author != null ? author : "") +
                ", comments=" + (comments != null ? comments : "") +
                '}';
    }
}
