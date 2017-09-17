package ru.hd.olaf.mvc.service.impl;

import com.google.common.collect.Lists;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hd.olaf.entities.User;
import ru.hd.olaf.mvc.repository.UserRepository;
import ru.hd.olaf.mvc.service.UserService;
import ru.hd.olaf.util.LogUtil;
import ru.hd.olaf.util.db.UserSortable;

import java.util.List;

/**
 * Created by d.v.hozyashev on 31.07.2017.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {
        return Lists.newArrayList(userRepository.findAll());
    }

    @Transactional
    public User save(User user) {
        logger.debug(LogUtil.getMethodName());

        return userRepository.save(user);
    }

    public User findOrCreate(Session session, String username, String profile) {
        logger.debug(LogUtil.getMethodName());
        logger.debug(String.format("Ищем в БД пользователя: %s", username));

        Query query = session.createQuery("SELECT u FROM User u WHERE u.username = :username");
        query.setString("username", username);

        List<User> users = (List<User>) query.list();

        User user;
        if (users.size() == 0) {
            user = new User(username, profile);
            session.save(user);
        } else
            user = users.get(0);

        return user;
//        List<User> users = Lists.newArrayList(userRepository.findByUsername(username));
//        return users != null && users.size() > 0 ? users.get(0) : new User(username, profile);
    }

    public long getTotalCount() {
        return userRepository.count();
    }

    public Page<UserSortable> getUsers(Pageable pageable) {
        logger.debug(LogUtil.getMethodName());
        return userRepository.getSummaryData(pageable);
    }
}
