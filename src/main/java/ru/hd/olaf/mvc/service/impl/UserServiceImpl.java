package ru.hd.olaf.mvc.service.impl;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hd.olaf.entities.User;
import ru.hd.olaf.mvc.repository.UserRepository;
import ru.hd.olaf.mvc.service.UserService;
import ru.hd.olaf.util.LogUtil;

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

    public User save(User user) {
        logger.debug(LogUtil.getMethodName());

        return userRepository.save(user);
    }
}
