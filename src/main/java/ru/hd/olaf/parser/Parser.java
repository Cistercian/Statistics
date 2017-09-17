package ru.hd.olaf.parser;

import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ru.hd.olaf.entities.Comment;
import ru.hd.olaf.entities.Topic;
import ru.hd.olaf.entities.User;
import ru.hd.olaf.mvc.service.CommentService;
import ru.hd.olaf.mvc.service.TopicService;
import ru.hd.olaf.mvc.service.UserService;
import ru.hd.olaf.util.HibernateUtil;
import ru.hd.olaf.util.LogUtil;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

/**
 * Created by d.v.hozyashev on 04.08.2017.
 * <p/>
 * класс для парсинга страницы с постом
 */
public class Parser {

    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    private static final String INDEX_URL = "https://pikabu.ru/hot?page=";
    private static final String CLASSNAME_USER = "b-comment";
    private static final short PAGES_TO_SCAN = 1;

    private static WebDriver webDriver;

    private static UserService userService;
    private static TopicService topicService;
    private static CommentService commentService;

    public static String parsingIndexPage(EntityManager entityManager, UserService uService, TopicService tService, CommentService cService) {
        logger.debug(LogUtil.getMethodName());

        System.setProperty("webdriver.gecko.driver", "C:\\Program Files\\Mozilla Firefox\\geckodriver.exe");
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        webDriver = new FirefoxDriver();
        userService = uService;
        topicService = tService;
        commentService = cService;

        StringBuilder message = new StringBuilder();
        int countPages;
        try {
            for (countPages = 1; countPages <= PAGES_TO_SCAN; countPages++) {

//                Document document = Jsoup.connect(INDEX_URL + countPages).timeout(0).get();
                webDriver.get(INDEX_URL + countPages);

                List<WebElement> stories = webDriver.findElements(By.className("story"));

                Set<Topic> topics = new HashSet<Topic>();
                for (WebElement story : stories) {
                    try {
                        Thread.sleep((long) (Math.random() * 0));

                        logger.debug(String.format("Обрабатывается история с id: %s", story.getAttribute("data-story-id")));
                        Integer storyId = Integer.parseInt(story.getAttribute("data-story-id"));

                        Topic topic = topicService.findOrCreateByTopicId(storyId);

                        WebElement storyData = story.findElement(By.className("story__header-title"))
                                .findElement(By.tagName("a"));

                        topic.setTitle(storyData.getText());
                        topic.setUrl(storyData.getAttribute("href"));

                        //поиск рейтинга
                        storyData = story.findElement(By.className("story__rating-count"));
                        topic.setRating(Integer.parseInt(storyData.getText()));

                        //поиск автора
                        if (topic.getAuthor() == null) {
                            storyData = story.findElement(By.className("story__author"));

                            Session session = HibernateUtil.getSessionFactory().openSession();
                            session.beginTransaction();
                            User author = userService.findOrCreate(session, storyData.getText(), storyData.getAttribute("href"));
                            session.getTransaction().commit();
                            session.close();

                            topic.setAuthor(author);
                            userService.save(author);
                        }

                        logger.debug(String.format("Получен объект Topic: %s", topic));

                        topics.add(topic);

                        //return "";
                    } catch (NumberFormatException e) {
                        logger.debug(String.format("Произошла ошибка при парсинге номера истории: %s. Достигли конца страницы", story.getAttribute("data-story-id")));
                    } catch (InterruptedException e) {
                        logger.debug(String.format("Произошла неожиданная остановка процесса обработки: %s", story.getAttribute("data-story-id")));
                    }
                }

                parsingTopics(topics);
            }
        } catch (Exception e) {
            String error = String.format("Произошла ошибка при получении страницы сайта: %s", e.getMessage());
            logger.debug(error);
            message.append(error);

            return message.toString();
        }

        logger.debug("Парсинг завершен!");

        message.append("Обработано страниц: ")
                .append(countPages);

        return message.toString();
    }

    private static void parsingTopics(Set<Topic> topics) {
        logger.debug(LogUtil.getMethodName());

        for (Topic topic : topics) {
            Map<User, Set<Comment>> map = parsingTopicPage(topic.getUrl());
            saveData(topic, map);
        }
    }

    private static Map<User, Set<Comment>> parsingTopicPage(String url) {
        logger.debug(LogUtil.getMethodName());
        logger.debug(String.format("Обрабатывается страница поста: %s", url));

        Map<User, Set<Comment>> result = new HashMap<User, Set<Comment>>();

        try {
            webDriver.get(url);

            while (true) {
                try {
                    WebElement element = webDriver.findElement(By.className("b-comments__next"));
                    if ("display: none;".equals(element.getAttribute("style")))
                        break;
                    else {
                        element.click();
                        webDriver.wait(1000);
                    }
                } catch (Exception e) {
                    logger.debug(e.getMessage());
                    break;
                }
            }
            //Document document = Jsoup.connect(url).timeout(0).get();
            //Elements elements = document.getElementsByClass(CLASSNAME_USER);
            List<WebElement> elements = webDriver.findElements(By.className(CLASSNAME_USER));

            for (WebElement elementComment : elements) {

                try {
                    Integer commentId = Integer.valueOf(elementComment.getAttribute("data-id"));


                    WebElement userData = elementComment.findElement(By.className("b-comment__user"))
                            .findElement(By.tagName("a"));

                    String userProfile = userData.getAttribute("href");
                    String userName = userData.findElement(By.tagName("span")).getText();

                    //User user = userService.findOrCreate(userName);
                    User user = new User(userName);
                    user.setProfile(userProfile);

                    //рейтинг пользователя
                    //getUserRating(user);

                    //Собираем данные комментария
                    Comment comment = new Comment(user, commentId);
                    //рейтинг комментария
                    userData = elementComment.findElement(By.className("b-comment__rating-count"));
                    try {
                        Integer rating = Integer.valueOf(userData.getText());
                        comment.setRating(rating);
                    } catch (NumberFormatException e) {
                        logger.debug(String.format("Ошибка при парсинге рейтинга комментария: %s", userData.getText()));
                    }

                    logger.debug(String.format("userName: %s, profile: %s, commentId: %s",
                            userName, userProfile, commentId));

                    Set<Comment> comments = !result.containsKey(user) ? new HashSet<Comment>() : result.get(user);

                    comments.add(comment);
                    result.put(user, comments);
                } catch (NumberFormatException e) {
                    logger.debug(String.format("Ошибка парсинга номера комментария: %s", elementComment.getAttribute("data-id")));
                }

            }
            logger.debug("Закончили парсить топик.");
        } catch (Exception e) {
            logger.debug(String.format("Ошибка парсинга страницы: %s", e.getMessage()));
            return null;
        }

        return result;
    }

    @Transactional
    private static boolean saveData(Topic topic, Map<User, Set<Comment>> map) {
        logger.debug(LogUtil.getMethodName());

        if (map == null)
            throw new IllegalArgumentException("Empty map");

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        topic = (Topic) session.merge(topic);
        //topic = topicService.save(topic);

        for (Map.Entry<User, Set<Comment>> entry : map.entrySet()) {
            logger.debug(String.format("Обрабатываем данные пользователя %s", entry.getKey()));

            User user = entry.getKey();
            user = userService.findOrCreate(session, user.getUsername(), user.getProfile());
            //getUserRating(user);

            for (Comment comment : entry.getValue()) {
                logger.debug(String.format("Комментарий id: %d", comment.getCommentId()));

                comment.setTopic(topic);
                comment.setUser(user);
//                topic.addComment(comment);
                //user.getComments().add(comment);
                //topic.getComments().add(comment);

//                session.saveOrUpdate(comment);
                commentService.save(session, comment);
            }
            //user = userService.save(user);
            //topic = topicService.save(topic);
        }

        logger.debug(String.format("Завершено сохранение данных топика %s.", topic.getTopicId()));

        session.getTransaction().commit();
        session.close();
        return true;
    }

    public static void getUserRating(User user) {
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