package ru.hd.olaf.mvc.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public Comment save(Comment comment) {
        logger.debug(LogUtil.getMethodName());
        return commentRepository.save(comment);
    }
}
