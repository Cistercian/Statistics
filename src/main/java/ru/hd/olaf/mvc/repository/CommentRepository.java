package ru.hd.olaf.mvc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hd.olaf.entities.Comment;
import ru.hd.olaf.entities.Topic;
import ru.hd.olaf.entities.User;

/**
 * Created by Olaf on 05.08.2017.
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Comment findByUserAndTopicAndCommentId(User user, Topic topic, Integer commentId);

    long count();

    Page<Comment> findAll(Pageable pageable);

}
