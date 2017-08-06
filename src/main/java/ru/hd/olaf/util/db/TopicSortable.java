package ru.hd.olaf.util.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hd.olaf.entities.User;

/**
 * Created by Olaf on 06.08.2017.
 */
public class TopicSortable {
    private String title;
    private String url;
    private Integer rating;
    private String author;
    private long countComments;

    public TopicSortable(String title, String url, Integer rating, String author, long countComments) {
        this.title = title;
        this.url = url;
        this.rating = rating;
        this.author = author;
        this.countComments = countComments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getCountComments() {
        return countComments;
    }

    public void setCountComments(long countComments) {
        this.countComments = countComments;
    }
}
