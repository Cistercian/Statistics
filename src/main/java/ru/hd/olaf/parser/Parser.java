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
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by d.v.hozyashev on 04.08.2017.
 * <p/>
 * класс для парсинга страницы с постом
 */
public class Parser {

    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    private static final String INDEX_URL = "https://pikabu.ru/hot?page=";

    private static final String CLASSNAME_USER = "b-comment";

    private static final short PAGES_TO_SCAN = 5;

    public static void parsingIndexPage(EntityManager entityManager, UserService userService, TopicService topicService, CommentService commentService) {
        logger.debug(LogUtil.getMethodName());

        try {
            for (short countPages = 1; countPages <= PAGES_TO_SCAN; countPages++) {

                Document document = Jsoup.connect(INDEX_URL + countPages).timeout(0).get();

                Elements stories = document.select("div.story");

                for (Element story : stories) {

                    try {
                        Thread.sleep((long) Math.random() * (long) 5000);

                        logger.debug(String.format("Обрабатывается история с id: %s", story.attr("data-story-id")));
                        Integer storyId = Integer.parseInt(story.attr("data-story-id"));

                        Topic topic = topicService.findOrCreateByTopicId(storyId);

                        Element storyData = story.getElementsByClass("story__header-title").get(0)
                                .getElementsByTag("a").get(0);

                        topic.setTitle(storyData.text());
                        topic.setUrl(storyData.attr("href"));

                        //поиск рейтинга
                        storyData = story.getElementsByClass("story__rating-count").get(0);
                        topic.setRating(Integer.parseInt(storyData.text()));

                        //поиск автора
                        if (topic.getAuthor() == null) {
                            storyData = story.getElementsByClass("story__author").get(0);

                            User author = userService.findOrCreate(storyData.text(), storyData.attr("href"));

                            topic.setAuthor(author);
                            //author.getWrittenTopics().add(topic);
                            userService.save(author);
                        }

                        logger.debug(String.format("Получен объект Topic: %s", topic));

                        Map<User, Set<Comment>> map = parsingTopicPage(topic.getUrl(), userService);

                        saveData(userService, topicService, commentService, topic, map);

                    } catch (NumberFormatException e) {
                        logger.debug(String.format("Произошла ошибка при парсинге номера истории: %s. Достигли конца страницы", story.attr("data-story-id")));
                    } catch (InterruptedException e) {
                        logger.debug(String.format("Произошла неожиданная остановка процесса обработки: %s", story.attr("data-story-id")));
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
        logger.debug(String.format("Обрабатывается страница поста: %s", url));

        //int counter = 0;

        Map<User, Set<Comment>> result = new HashMap<User, Set<Comment>>();

        try {
            Document document = Jsoup.connect(url).timeout(0).get();

            Elements elements = document.getElementsByClass(CLASSNAME_USER);

            for (Element elementComment : elements) {

                try {
                    Integer commentId = Integer.valueOf(elementComment.attr("data-id"));

                    Element userData = elementComment.getElementsByClass("b-comment__user").get(0)
                            .getElementsByTag("a").get(0);

                    String userProfile = userData.attr("href");
                    String userName = userData.getElementsByTag("span").get(0).text();

                    //User user = userService.findOrCreate(userName);
                    User user = new User(userName);
                    user.setProfile(userProfile);

                    //рейтинг пользователя
                    //getUserRating(user);

                    //Собираем данные комментария
                    Comment comment = new Comment(user, commentId);
                    //рейтинг комментария
                    userData = elementComment.getElementsByClass("b-comment__rating-count").get(0);
                    try {
                        Integer rating = Integer.valueOf(userData.text());
                        comment.setRating(rating);
                    } catch (NumberFormatException e) {
                        logger.debug(String.format("Ошибка при парсинге рейтинга комментария: %s", userData.text()));
                    }

                    logger.debug(String.format("userName: %s, profile: %s, commentId: %s",
                            userName, userProfile, commentId));

                    Set<Comment> comments = !result.containsKey(user) ? new HashSet<Comment>() : result.get(user);

                    comments.add(comment);
                    result.put(user, comments);

//                    if (++counter > 50)
//                        return result;
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

        //topic = topicService.save(topic);

        for (Map.Entry<User, Set<Comment>> entry : map.entrySet()){
            logger.debug(String.format("Обрабатываем данные пользователя %s", entry.getKey()));

            User user = entry.getKey();
            user = userService.findOrCreate(user.getUsername(), user.getProfile());
            //getUserRating(user);
            user = userService.save(user);

            for (Comment comment : entry.getValue()){
                logger.debug(String.format("Комментарий id: %d", comment.getCommentId()));

                //topic.getComments().add(comment);
                comment.setTopic(topic);
                comment.setUser(user);
                //user.getComments().add(comment);
                commentService.save(comment); //!!!
            }
            //userService.save(user);
        }

        map = null;

        Topic result = topicService.save(topic);
        logger.debug(result.toString());

        return true;
    }

    public static void getUserRating(User user){
        logger.debug(LogUtil.getMethodName());
        logger.debug(String.format("Refreshing user: %s", user.getUsername()));

        try {
            Document document = Jsoup.connect(user.getProfile()).timeout(0).get();

            Element profileData = document.getElementsByClass("b-user-profile__value").get(1);

            try {
                Integer rating = Integer.valueOf(profileData.text());
                user.setRating(rating);
            } catch (NumberFormatException e) {
                logger.debug(String.format("Ошибка при парсинге рейтинга комментария: %s", profileData.text()));
            }

        } catch (IOException e) {
            logger.debug(String.format("Возникла ошибка при открытии профиля пользователя: %s, %s, %s",
                    user.getUsername(), user.getProfile(), e.getMessage()));
        }
    }
}