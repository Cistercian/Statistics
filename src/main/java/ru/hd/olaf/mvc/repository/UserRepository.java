package ru.hd.olaf.mvc.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.hd.olaf.entities.User;

/**
 * Created by d.v.hozyashev on 31.07.2017.
 */
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);

    @Query("SELECT COUNT(id) FROM User ")
    Integer getTotalCount();
}
