package ru.hd.olaf.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hd.olaf.entities.Comment;

/**
 * Created by Olaf on 05.08.2017.
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
