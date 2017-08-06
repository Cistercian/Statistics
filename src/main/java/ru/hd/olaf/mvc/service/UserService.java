package ru.hd.olaf.mvc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.hd.olaf.entities.User;
import ru.hd.olaf.util.db.UserSortable;

import java.util.List;

/**
 * Created by d.v.hozyashev on 31.07.2017.
 */
public interface UserService {

    List<User> getAll();

    User save(User user);

    User findOrCreate(String username, String profile);

    long getTotalCount();

    Page<UserSortable> getUsers(Pageable pageable);
}
