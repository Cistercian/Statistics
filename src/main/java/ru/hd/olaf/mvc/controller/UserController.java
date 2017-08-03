package ru.hd.olaf.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.hd.olaf.mvc.service.UserService;

/**
 * Created by d.v.hozyashev on 31.07.2017.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getUsers(){
        ModelAndView modelAndView = new ModelAndView("index");

        modelAndView.addObject("users", userService.getAll());

        return modelAndView;
    }
}
