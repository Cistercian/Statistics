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

    private static final String INDEX_URL = "https://pikabu.ru/hot?page=";

    private static final String CLASSNAME_USER = "b-comment";

    private static final short COUNT_SCAN = 3;

    public static void parsingIndexPage(EntityManager entityManager, UserService userService, TopicService topicService, CommentService commentService) {
        logger.debug(LogUtil.getMethodName());

        try {
            for (short countPages = 1; countPages <= COUNT_SCAN; countPages++) {

                Document document = Jsoup.connect(INDEX_URL + countPages).get();

                Elements stories = document.select("div.story");

                for (Element story : stories) {

                    try {
                        Thread.sleep((long) Math.random() * (long) 5000);

                        logger.debug(String.format("Обрабатывается история с id: %s", story.attr("data-story-id")));
                        Integer storyId = Integer.parseInt(story.attr("data-story-id"));

                        Topic topic = topicService.findOrCreateByTopicId(storyId);

                        Element storyHeader = story.getElementsByClass("story__header-title").get(0)
                                .getElementsByTag("a").get(0);

                        topic.setTitle(storyHeader.text());
                        topic.setURL(storyHeader.attr("href"));

                        //поиск автора
                        storyHeader = story.getElementsByClass("story__author").get(0);

                        User author = userService.findOrCreate(storyHeader.text());
                        author.setProfile(storyHeader.attr("href"));

                        topic.setAuthor(author);
                        author.getWrittenTopics().add(topic);
                        userService.save(author);

                        logger.debug(String.format("Получен объект Topic: %s", topic));

                        Map<User, Set<Comment>> map = parsingTopicPage(topic.getURL(), userService);

                        saveData(userService, topicService, commentService, topic, map);

                    } catch (NumberFormatException e) {
                        logger.debug(String.format("Произошла ошибка при парсинге номера истории: %s. Достигли конца страницы", story.attr("data-story-id")));
                    } catch (InterruptedException e) {
                        logger.debug(String.format("Произошла неожиданная остановка процесса обработки", story.attr("data-story-id")));
                    }
                }
            }

        } catch (IOException e) {
            logger.debug(String.format("Произошла ошибка при получении страницы сайта: %s", INDEX_URL));
        }

        logger.debug("Парсинг завершен!");
    }

    private static Map<User, Set<Comment>> parsingTopicPage(String url, UserService userService) {
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

                    User user = userService.findOrCreate(userName);
                    user.setProfile(userProfile);
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

        topic = topicService.save(topic);

        for (Map.Entry<User, Set<Comment>> entry : map.entrySet()){
            logger.debug(String.format("Обрабатываем данные пользователя %s", entry.getKey()));

            User user = userService.save(entry.getKey());

            for (Comment comment : entry.getValue()){
                logger.debug(String.format("Комментарий id: %d", comment.getCommentId()));


                //topic.getComments().add(comment);
                comment.setTopic(topic);
                comment.setUser(user);
                //user.getComments().add(comment);
                commentService.save(comment);
            }

            //userService.save(user);
        }

        Topic result = topicService.save(topic);
        logger.debug(result.toString());

        return true;
    }

}