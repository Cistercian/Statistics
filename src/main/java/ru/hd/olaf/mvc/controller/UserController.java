package ru.hd.olaf.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.hd.olaf.mvc.repository.CommentRepository;
import ru.hd.olaf.mvc.service.CommentService;
import ru.hd.olaf.mvc.service.TopicService;
import ru.hd.olaf.mvc.service.UserService;
import ru.hd.olaf.parser.Parser;
import ru.hd.olaf.util.LogUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Created by d.v.hozyashev on 31.07.2017.
 */
@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndexPage(Model model){
        logger.debug(LogUtil.getMethodName());
        model.addAttribute("title", "JavaConfig index page");

        return "index";
    }

    @RequestMapping(value = "/testParsing", method = RequestMethod.GET)
    public void testParsing(){
        logger.debug(LogUtil.getMethodName());

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Parser.parsingIndexPage(entityManager, userService, topicService, commentService);

    }
}
