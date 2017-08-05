package ru.hd.olaf.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hd.olaf.entities.Topic;

/**
 * Created by Olaf on 05.08.2017.
 */
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    Topic findByTopicId(Integer topicId);

}
