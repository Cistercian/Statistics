package ru.hd.olaf.util.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Olaf on 06.08.2017.
 */
public class UserSortable {

    private static final Logger logger = LoggerFactory.getLogger(UserSortable.class);

    private String username;
    private Integer rating;
    private String profile;
    private Integer countWrittenTopics;
    private Integer countComments;

    public UserSortable(String username, Integer rating, String profile, long countWrittenTopics, long countComments) {
        this.username = (username != null ? username : "");
        this.rating = rating;
        this.profile = (profile != null ? profile : "");
        this.countWrittenTopics = (int)countWrittenTopics;
        this.countComments = (int)countComments;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Integer getCountWrittenTopics() {
        return countWrittenTopics;
    }

    public void setCountWrittenTopics(Integer countWrittenTopics) {
        this.countWrittenTopics = countWrittenTopics;
    }

    public Integer getCountComments() {
        return countComments;
    }

    public void setCountComments(Integer countComments) {
        this.countComments = countComments;
    }
}
