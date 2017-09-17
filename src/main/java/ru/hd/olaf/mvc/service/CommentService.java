package ru.hd.olaf.mvc.service;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.hd.olaf.entities.Comment;

/**
 * Created by Olaf on 05.08.2017.
 */
public interface CommentService {

    void save(Session session, Comment comment);

    long getTotalCount();

    Page<Comment> getComments(Pageable pageable);
}
