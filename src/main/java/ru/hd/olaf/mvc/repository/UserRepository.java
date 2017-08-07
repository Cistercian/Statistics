package ru.hd.olaf.mvc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.hd.olaf.entities.User;
import ru.hd.olaf.util.db.UserSortable;

import java.util.List;

/**
 * Created by d.v.hozyashev on 31.07.2017.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findByUsername(String username);

    long count();

    Page<User> findAll(Pageable pageable);

    @Query("SELECT new ru.hd.olaf.util.db.UserSortable(" +
            "u.username, u.rating, u.profile, COUNT(DISTINCT t.id) AS countWrittenTopics, COUNT(c.id) AS countComments" +
            ") FROM User u LEFT JOIN u.writtenTopics t LEFT JOIN u.comments c " +
            "GROUP BY u.id ")
    Page<UserSortable> getSummaryData(Pageable pageable);
}
