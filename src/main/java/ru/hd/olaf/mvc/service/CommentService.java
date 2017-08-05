package ru.hd.olaf.mvc.service;

import ru.hd.olaf.entities.Comment;

/**
 * Created by Olaf on 05.08.2017.
 */
public interface CommentService {

    Comment save(Comment comment);

    Integer getTotalCount();
}
