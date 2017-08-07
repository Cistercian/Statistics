package ru.hd.olaf.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by d.v.hozyashev on 04.08.2017.
 */
@Entity
@Table(name = "comments", catalog = "parsed_site")
public class Comment {
    private Integer id;
    private Topic topic;
    private User user;
    private Integer commentId;
    private Integer rating;

    public Comment() {
    }

    public Comment(User user, Integer commentId) {
        this.user = user;
        this.commentId = commentId;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "topic")
    @JsonBackReference
    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user")
    @JsonBackReference
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Basic
    @Column(name = "comment_id", nullable = true)
    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    @Basic
    @Column(name = "rating")
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (id != null ? !id.equals(comment.id) : comment.id != null) return false;
        if (topic != null ? !topic.equals(comment.topic) : comment.topic != null) return false;
        if (user != null ? !user.equals(comment.user) : comment.user != null) return false;
        return commentId != null ? commentId.equals(comment.commentId) : comment.commentId == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (topic != null ? topic.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (commentId != null ? commentId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", topic=" + (topic != null ? topic : "") +
                ", user=" + (topic != null ? topic : "") +
                ", commentId=" + (topic != null ? topic : "") +
                '}';
    }
}
