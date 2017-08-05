package ru.hd.olaf.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hd.olaf.entities.Comment;
import ru.hd.olaf.entities.Topic;
import ru.hd.olaf.entities.User;

/**
 * Created by Olaf on 05.08.2017.
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Comment findByUserAndTopicAndCommentId(User user, Topic topic, Integer commentId);

    @Query("SELECT COUNT(id) FROM Comment ")
    Integer getTotalCount();

}
