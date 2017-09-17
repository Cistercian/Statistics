package ru.hd.olaf.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.hd.olaf.entities.User;
import ru.hd.olaf.mvc.service.CommentService;
import ru.hd.olaf.mvc.service.TopicService;
import ru.hd.olaf.mvc.service.UserService;
import ru.hd.olaf.parser.Parser;
import ru.hd.olaf.util.LogUtil;

import java.util.List;

/**
 * Created by Olaf on 06.08.2017.
 */
@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private CommentService commentService;
//    @Autowired
//    private EntityManagerFactory entityManagerFactory;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndexPage(Model model, Pageable pageable){
        logger.debug(LogUtil.getMethodName());
        model.addAttribute("title", "Statistic data");

        model.addAttribute("usersCount", userService.getTotalCount());
        model.addAttribute("topicsCount", topicService.getTotalCount());
        model.addAttribute("commentsCount", commentService.getTotalCount());

        return "index";
    }

    @RequestMapping(value = "/testParsing", method = RequestMethod.GET)
    public @ResponseBody String testParsing(){
        logger.debug(LogUtil.getMethodName());

        return Parser.parsingIndexPage(null, userService, topicService, commentService);
    }

    @RequestMapping(value = "/refreshUsersRating", method = RequestMethod.GET)
    public void refreshUsersRating(){
        logger.debug(LogUtil.getMethodName());

        List<User> users = userService.getAll();

        for (User user : users) {
            Parser.getUserRating(user);
            userService.save(user);
        }
    }
}
