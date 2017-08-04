package ru.hd.olaf.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hd.olaf.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by d.v.hozyashev on 04.08.2017.
 *
 * класс для парсинга страницы с постом
 */
public class TopicParser {

    private static final Logger logger = LoggerFactory.getLogger(TopicParser.class);

    private static final String CLASSNAME_USER = "b-comment";

    public static void parsingTopicPage(){
        logger.debug(LogUtil.getMethodName());

        File file = Paths.get("c:/1/java/Statistics/src/main/resources/template/topic.html").toFile();

        if (file.isFile()){

            try {
                Document document = Jsoup.parse(file, "ISO-8859-9");
                logger.debug("Начинаем парсить файл.");
                Elements elements = document.getElementsByClass(CLASSNAME_USER);

                for (Element comment : elements){

                    String commentId = comment.attr("data-id");

                    Element userBranch = comment.getElementsByClass("b-comment__body").get(0)
                            .getElementsByClass("b-comment__header").get(0)
                            .getElementsByClass("b-comment__user").get(0)
                            .getElementsByTag("a").get(0);


                    String userProfile = userBranch.attr("href");
                    String userName = userBranch.getElementsByTag("span").get(0).text();

                    logger.debug(String.format("userName: %s, profile: %s, commentId: %s",
                            userName, userProfile, commentId));

                }
                logger.debug("Закончили парсить файл.");
            } catch (IOException e) {
                logger.debug(String.format("Ошибка чтения файла: %s", e.getMessage()));
            }
        } else {
            logger.debug("Обрабатываемый файл не найден!");
        }
    }

}