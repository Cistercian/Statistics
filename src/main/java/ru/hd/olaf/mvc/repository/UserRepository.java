package ru.hd.olaf.mvc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.hd.olaf.entities.User;

/**
 * Created by d.v.hozyashev on 31.07.2017.
 */
public interface UserRepository extends CrudRepository<User, Integer> {

}
