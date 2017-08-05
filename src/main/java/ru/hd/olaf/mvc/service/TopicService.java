package ru.hd.olaf.mvc.service;

import ru.hd.olaf.entities.Topic;

/**
 * Created by Olaf on 05.08.2017.
 */
public interface TopicService {

    Topic findOrCreateByTopicId(Integer topicId);

    Topic save(Topic topic);

    Integer getTotalCount();
}
