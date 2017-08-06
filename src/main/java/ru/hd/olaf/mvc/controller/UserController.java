package ru.hd.olaf.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.hd.olaf.entities.Topic;
import ru.hd.olaf.entities.User;
import ru.hd.olaf.mvc.repository.CommentRepository;
import ru.hd.olaf.mvc.service.CommentService;
import ru.hd.olaf.mvc.service.TopicService;
import ru.hd.olaf.mvc.service.UserService;
import ru.hd.olaf.parser.Parser;
import ru.hd.olaf.util.LogUtil;
import ru.hd.olaf.util.db.UserSortable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Iterator;
import java.util.List;

/**
 * Created by d.v.hozyashev on 31.07.2017.
 */
@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/data/users", method = RequestMethod.GET)
    public String getUsersPage(Model model, Pageable pageable){
        logger.debug(LogUtil.getMethodName());

        model.addAttribute("currentPage", pageable.getPageNumber());

        String sortDirection = "DESC";
        if (pageable.getSort() != null) {
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            sortDirection = iterator.hasNext() ? iterator.next().getDirection().name() : sortDirection;
        }

        sortDirection = "DESC".equalsIgnoreCase(sortDirection) ? "ASC" : "DESC";
        model.addAttribute("sortDirection", sortDirection);

        List<UserSortable> users = userService.getUsers(pageable).getContent();
        model.addAttribute("users", users);

        return "data/users";
    }

}
