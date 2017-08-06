package ru.hd.olaf.mvc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hd.olaf.entities.Topic;
import ru.hd.olaf.util.db.TopicSortable;
import ru.hd.olaf.util.db.UserSortable;

import java.util.List;

/**
 * Created by Olaf on 05.08.2017.
 */
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    Topic findByTopicId(Integer topicId);

    long count();

    Page<Topic> findAll(Pageable pageable);

    @Query("SELECT new ru.hd.olaf.util.db.TopicSortable(" +
            "t.title, t.url, t.rating, u.username, COUNT(c.id) AS countComments" +
            ") FROM Topic t JOIN t.comments c JOIN t.author u " +
            "GROUP BY t.id ")
    Page<TopicSortable> getSummaryData(Pageable pageable);
}
