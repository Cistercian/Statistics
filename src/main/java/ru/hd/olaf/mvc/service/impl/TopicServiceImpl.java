package ru.hd.olaf.mvc.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.hd.olaf.entities.Topic;
import ru.hd.olaf.mvc.repository.TopicRepository;
import ru.hd.olaf.mvc.service.TopicService;
import ru.hd.olaf.util.LogUtil;

/**
 * Created by Olaf on 05.08.2017.
 */
@Service
public class TopicServiceImpl implements TopicService {

    private static final Logger logger = LoggerFactory.getLogger(TopicServiceImpl.class);

    @Autowired
    private TopicRepository topicRepository;

    public Topic findOrCreateByTopicId(Integer topicId) {
        logger.debug(LogUtil.getMethodName());

        Topic topic = topicRepository.findByTopicId(topicId);
        return topic ==  null ? new Topic(topicId) : topic;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public Topic save(Topic topic) {
        logger.debug(LogUtil.getMethodName());
        logger.debug(String.format("Entity Topic for save: %s", topic));

        Topic result = topic;
        try {
            result = topicRepository.saveAndFlush(topic);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }

        return result;
    }
}
