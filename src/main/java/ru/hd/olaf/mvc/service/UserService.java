package ru.hd.olaf.mvc.service;

import ru.hd.olaf.entities.User;

import java.util.List;

/**
 * Created by d.v.hozyashev on 31.07.2017.
 */
public interface UserService {

    List<User> getAll();

    User save(User user);

}
