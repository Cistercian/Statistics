package ru.hd.olaf.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Created by Olaf on 17.09.2017.
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    static {
        Configuration configuration = new Configuration().addResource("hibernate.cfg.xml");
        configuration.addAnnotatedClass(ru.hd.olaf.entities.Comment.class);
        configuration.addAnnotatedClass(ru.hd.olaf.entities.Topic.class);
        configuration.addAnnotatedClass(ru.hd.olaf.entities.User.class);
        configuration.configure();

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(builder.build());
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
