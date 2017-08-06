package ru.hd.olaf.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.hd.olaf.entities.Comment;
import ru.hd.olaf.entities.User;
import ru.hd.olaf.mvc.service.CommentService;
import ru.hd.olaf.mvc.service.UserService;
import ru.hd.olaf.util.LogUtil;

import java.util.List;

/**
 * Created by Olaf on 06.08.2017.
 */
@Controller
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/data/comments", method = RequestMethod.GET)
    public String getUsersPage(Model model, Pageable pageable) {
        logger.debug(LogUtil.getMethodName());

        model.addAttribute("currentPage", pageable.getPageNumber());

        List<Comment> comments = commentService.getComments(pageable).getContent();
        model.addAttribute("comments", comments);

        return "data/comments";
    }
}
