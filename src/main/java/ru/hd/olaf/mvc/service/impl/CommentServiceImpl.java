package ru.hd.olaf.mvc.service.impl;

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

/**
 * Created by Olaf on 05.08.2017.
 */
@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public Comment save(Comment comment) {
        logger.debug(LogUtil.getMethodName());

        Comment existed = commentRepository.findByUserAndTopicAndCommentId(comment.getUser(), comment.getTopic(), comment.getCommentId());

        if (existed == null)
            return commentRepository.save(comment);
        else
            return existed;
    }

    public long getTotalCount() {
        return commentRepository.count();
    }

    public Page<Comment> getComments(Pageable pageable) {
        logger.debug(LogUtil.getMethodName());
        return commentRepository.findAll(pageable);
    }
}
