package ru.hd.olaf.mvc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.hd.olaf.entities.Topic;
import ru.hd.olaf.util.db.TopicSortable;

/**
 * Created by Olaf on 05.08.2017.
 */
public interface TopicService {

    Topic findOrCreateByTopicId(Integer topicId);

    Topic save(Topic topic);

    long getTotalCount();

    Page<TopicSortable> getTopics(Pageable pageable);
}
