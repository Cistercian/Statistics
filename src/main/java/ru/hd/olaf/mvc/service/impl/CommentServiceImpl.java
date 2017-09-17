package ru.hd.olaf.mvc.service.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hd.olaf.entities.Comment;
import ru.hd.olaf.mvc.repository.CommentRepository;
import ru.hd.olaf.mvc.service.CommentService;
import ru.hd.olaf.util.LogUtil;

import java.util.List;

/**
 * Created by Olaf on 05.08.2017.
 */
@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public void save(Session session, Comment comment) {
        logger.debug(LogUtil.getMethodName());

        Query query = session.createQuery("SELECT c FROM Comment c LEFT JOIN c.user u LEFT JOIN c.topic t " +
                "WHERE u = :user AND t = :topic AND c.commentId = :commentId");

        query.setParameter("user", comment.getUser());
        query.setParameter("topic", comment.getTopic());
        query.setParameter("commentId", comment.getCommentId());

        List<Comment> comments = (List<Comment>) query.list();

        if (comments.size() == 0){
            session.save(comment);
        }

//        Comment existed = commentRepository.findByUserAndTopicAndCommentId(comment.getUser(), comment.getTopic(), comment.getCommentId());
//
//        if (existed == null)
//            return commentRepository.save(comment);
//        else
//            return existed;
    }

    public long getTotalCount() {
        return commentRepository.count();
    }

    public Page<Comment> getComments(Pageable pageable) {
        logger.debug(LogUtil.getMethodName());
        return commentRepository.findAll(pageable);
    }
}
