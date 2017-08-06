package ru.hd.olaf.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.hd.olaf.entities.Topic;
import ru.hd.olaf.mvc.service.TopicService;
import ru.hd.olaf.util.LogUtil;
import ru.hd.olaf.util.db.TopicSortable;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Olaf on 06.08.2017.
 */
@Controller
public class TopicController {

    private static final Logger logger = LoggerFactory.getLogger(TopicController.class);

    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "/data/topics", method = RequestMethod.GET)
    public String getTopicsPage(Model model, Pageable pageable) {
        logger.debug(LogUtil.getMethodName());

        model.addAttribute("currentPage", pageable.getPageNumber());

        String sortDirection = "DESC";
        if (pageable.getSort() != null) {
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            sortDirection = iterator.hasNext() ? iterator.next().getDirection().name() : sortDirection;
        }

        sortDirection = "DESC".equalsIgnoreCase(sortDirection) ? "ASC" : "DESC";
        model.addAttribute("sortDirection", sortDirection);

        List<TopicSortable> topics = topicService.getTopics(pageable).getContent();
        model.addAttribute("topics", topics);

        logger.debug(String.format("Size of data: %d", topics.size()));

        return "data/topics";
    }
}
