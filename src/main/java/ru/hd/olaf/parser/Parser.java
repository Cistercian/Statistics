package ru.hd.olaf.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ru.hd.olaf.entities.Comment;
import ru.hd.olaf.entities.Topic;
import ru.hd.olaf.entities.User;
import ru.hd.olaf.mvc.service.CommentService;
import ru.hd.olaf.mvc.service.TopicService;
import ru.hd.olaf.mvc.service.UserService;
import ru.hd.olaf.util.LogUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.util.*;

/**
 * Created by d.v.hozyashev on 04.08.2017.
 * <p/>
 * класс для парсинга страницы с постом
 */
public class Parser {

    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    private static final String INDEX_URL = "https://pikabu.ru/hot";

    private static final String CLASSNAME_USER = "b-comment";

    private static final short COUNT_SCAN = 2;

    public static void parsingIndexPage(EntityManager entityManager, UserService userService, TopicService topicService, CommentService commentService) {
        logger.debug(LogUtil.getMethodName());

        short count = 0;

        try {
            Document document = Jsoup.connect(INDEX_URL).get();

            Elements stories = document.select("div.story");

            for (Element story : stories) {

                if (++count > COUNT_SCAN) {
                    return;
                }

                try {
                    logger.debug(String.format("Обрабатывается история с id: %s", story.attr("data-story-id")));
                    Integer storyId = Integer.parseInt(story.attr("data-story-id"));

                    Topic topic = topicService.findOrCreateByTopicId(storyId);

                    Element storyHeader = story.getElementsByClass("story__header-title").get(0)
                            .getElementsByTag("a").get(0);

                    topic.setTitle(storyHeader.text());
                    topic.setURL(storyHeader.attr("href"));

                    //поиск автора
                    storyHeader = story.getElementsByClass("story__author").get(0);

                    User author = new User(storyHeader.text(), storyHeader.attr("href"));
                    topic.setAuthor(author);
                    author.getWrittenTopics().add(topic);
                    userService.save(author);

                    logger.debug(String.format("Получен объект Topic: %s", topic));

                    Map<User, Set<Comment>> map = parsingTopicPage(topic.getURL());

                    saveData(userService, topicService, commentService, topic, map);

                } catch (NumberFormatException e) {
                    logger.debug(String.format("Произошла ошибка при парсинге номера истории: %s", story.attr("data-story-id")));
                }
            }

        } catch (IOException e) {
            logger.debug(String.format("Произошла ошибка при получении страницы сайта: %s", INDEX_URL));
        }
    }

    private static Map<User, Set<Comment>> parsingTopicPage(String url) {
        logger.debug(LogUtil.getMethodName());
        logger.debug(String.format("Обрабатывается страницы поста: %s", url));


        Map<User, Set<Comment>> result = new HashMap<User, Set<Comment>>();

        try {
            Document document = Jsoup.connect(url).get();

            Elements elements = document.getElementsByClass(CLASSNAME_USER);

            for (Element elementComment : elements) {

                try {
                    Integer commentId = Integer.valueOf(elementComment.attr("data-id"));

                    Element userBranch = elementComment.getElementsByClass("b-comment__body").get(0)
                            .getElementsByClass("b-comment__header").get(0)
                            .getElementsByClass("b-comment__user").get(0)
                            .getElementsByTag("a").get(0);

                    String userProfile = userBranch.attr("href");
                    String userName = userBranch.getElementsByTag("span").get(0).text();

                    User user = new User(userName, userProfile);
                    Comment comment = new Comment(user, commentId);

                    logger.debug(String.format("userName: %s, profile: %s, commentId: %s",
                            userName, userProfile, commentId));

                    Set<Comment> comments = !result.containsKey(user) ? new HashSet<Comment>() : result.get(user);

                    comments.add(comment);
                    result.put(user, comments);

                } catch (NumberFormatException e) {
                    logger.debug(String.format("Ошибка парсинга номера комментария: %s", elementComment.attr("data-id")));
                }

            }
            logger.debug("Закончили парсить топик.");
        } catch (IOException e) {
            logger.debug(String.format("Ошибка парсинга страницы: %s", e.getMessage()));
            return null;
        }

        return result;
    }

    @Transactional
    private static boolean saveData(UserService userService, TopicService topicService, CommentService commentService, Topic topic, Map<User, Set<Comment>> map){
        logger.debug(LogUtil.getMethodName());

        for (Map.Entry<User, Set<Comment>> entry : map.entrySet()){
            logger.debug(String.format("Обрабатываем данные пользователя %s", entry.getKey()));
            User user = entry.getKey();

            for (Comment comment : entry.getValue()){
                logger.debug(String.format("Комментарий id: %d", comment.getCommentId()));

                topic.getComments().add(comment);
                comment.setTopic(topic);
                user.getComments().add(comment);

                commentService.save(comment);
            }

            userService.save(user);
        }

        Topic result = topicService.save(topic);
        logger.debug(result.toString());

        return true;
    }

}