package ru.hd.olaf.util.db;

/**
 * Created by Olaf on 06.08.2017.
 */
public class UserSortable {
    private String username;
    private Integer rating;
    private String profile;
    private Integer countWrittenTopics;
    private Integer countComments;

    public UserSortable(String username, int rating, String profile, long countWrittenTopics, long countComments) {
        this.username = username;
        this.rating = rating;
        this.profile = profile;
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
