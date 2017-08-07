package ru.hd.olaf.mvc.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.hd.olaf.entities.Topic;
import ru.hd.olaf.mvc.repository.TopicRepository;
import ru.hd.olaf.mvc.service.TopicService;
import ru.hd.olaf.util.LogUtil;
import ru.hd.olaf.util.db.TopicSortable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

/**
 * Created by Olaf on 05.08.2017.
 */
@Service
public class TopicServiceImpl implements TopicService {

    private static final Logger logger = LoggerFactory.getLogger(TopicServiceImpl.class);

    @Autowired
    private TopicRepository topicRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public Topic findOrCreateByTopicId(Integer topicId) {
        logger.debug(LogUtil.getMethodName());

        Topic topic = topicRepository.findByTopicId(topicId);
        return topic ==  null ? new Topic(topicId) : topic;
    }

    @Transactional
    public Topic save(Topic topic) {
        logger.debug(LogUtil.getMethodName());

        Topic result = topic;
        try {
            result = topicRepository.save(topic);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }

        return result;
    }

    public long getTotalCount() {
        logger.debug(LogUtil.getMethodName());
        return topicRepository.count();
    }

    public Page<TopicSortable> getTopics(Pageable pageable) {
        logger.debug(LogUtil.getMethodName());
        return topicRepository.getSummaryData(pageable);
    }
}
