package ru.hd.olaf.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.hd.olaf.mvc.service.UserService;
import ru.hd.olaf.parser.TopicParser;
import ru.hd.olaf.util.LogUtil;

/**
 * Created by d.v.hozyashev on 31.07.2017.
 */
@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndexPage(Model model){
        logger.debug(LogUtil.getMethodName());
        model.addAttribute("title", "JavaConfig index page");

        return "index";
    }

    @RequestMapping(value = "/testParsing", method = RequestMethod.GET)
    public void testParsing(){
        logger.debug(LogUtil.getMethodName());

        TopicParser.parsingTopicPage();

    }
}
