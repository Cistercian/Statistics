package ru.hd.olaf.config;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

/**
 * Created by d.v.hozyashev on 04.08.2017.
 * Конфигурация JPA
 *
 * аналог appconfig-data.xml
 */
@Configuration                                              //данный класс является Java Configuration;
@PropertySource(value = "classpath:application.properties") //<context:property-placeholder location=".." />
@EnableTransactionManagement                                //<tx:annotation-driven/>
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager",
        basePackages = {"ru.hd.olaf.mvc.repository"})         //<jpa:repositories base-package
@ComponentScan(basePackages = "ru.hd.olaf.entities")
public class DataConfig {

    /**
     * Подключаем аннотацию @PropertySource к спрингу
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Value("${jdbc.driverClassName}")
    private String jdbcDriverClassName;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbcPassword;

    /**
     * Аналог <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
     */
    @Bean(name = "dataSource")
    public DriverManagerDataSource getDriverManagerDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(jdbcDriverClassName);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUsername);
        dataSource.setPassword(jdbcPassword);
        return dataSource;
    }

    /**
     * Аналог <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
     * entityManagerFactory- обязательное наименование??
     */
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean getLocalContainerEntityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setPackagesToScan("ru.hd.olaf.entities"); //можно указать несколько значений
        emf.setDataSource(getDriverManagerDataSource());

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        ((HibernateJpaVendorAdapter)vendorAdapter).setGenerateDdl(true);
//        ((HibernateJpaVendorAdapter)vendorAdapter).setShowSql(true);
        emf.setJpaVendorAdapter(vendorAdapter);

        //аналог persistence.xml
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.show_sql",false);
        jpaProperties.put("hibernate.format_sql",false);
        jpaProperties.put("hibernate.connection.driver_class","com.mysql.jdbc.Driver");
        jpaProperties.put("hibernate.dialect","org.hibernate.dialect.MySQL5Dialect");
        jpaProperties.put("hibernate.hbm2ddl.auto","validate");
        jpaProperties.put("hibernate.jdbc.batch_size","50");
        jpaProperties.put("hibernate.connection.CharSet","utf8");
        jpaProperties.put("hibernate.connection.characterEncoding","utf8");
        jpaProperties.put("hibernate.connection.useUnicode","true");


        emf.setJpaProperties(jpaProperties);
        return emf;
    }

    /**
     * Аналог <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
     *
     * @return
     */
    @Bean(name = "jpaTransactionManager")
    public JpaTransactionManager getJpaTransactionManager() {
        JpaTransactionManager jpa = new JpaTransactionManager();
        jpa.setEntityManagerFactory(getLocalContainerEntityManagerFactoryBean().getNativeEntityManagerFactory());
        return jpa;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory em) {
        return new JpaTransactionManager(em);
    }
}
