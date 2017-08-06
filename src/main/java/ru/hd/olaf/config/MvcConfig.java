package ru.hd.olaf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Created by d.v.hozyashev on 04.08.2017.
 * Конфигурация MVC
 *
 * аналог appconfig-mvc.xml
 */
@EnableWebMvc                                       //<mvc:annotation-driven>
@Configuration                                      //данный класс является Java Configuration;
@ComponentScan(basePackages = {"ru.hd.olaf.mvc"})   //<context:component-scan base-package=''>
@EnableSpringDataWebSupport
public class MvcConfig extends WebMvcConfigurerAdapter {

    /**
     * Определяем где лежат ресурсы проекта, такие как css, image, js и другие
     * аналог mvc:resources mapping
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    /**
     * Описываем бин вьюресолвера
     * Аналог bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
     *
     * @return
     */
    @Bean
    public InternalResourceViewResolver setupViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/view/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);

        return resolver;
    }

    /**
     * Аналог <mvc:view-controller path=...></mvc:view-controller>
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        super.addViewControllers(registry);

        registry.addViewController("/").setViewName("/index");
        registry.addViewController("/index.html").setViewName("/index");
    }

    /**
     * Аналог <bean id="messageSource" class="org.springframework.context.support.ReloadableResourceBundleMessageSource">
     *
     * Функция не наследуется... почему?
     */
    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource getMessageSource() {
        ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
        resource.setBasename("classpath:/locales/messages");
        resource.setCacheSeconds(1);
        resource.setDefaultEncoding("UTF-8");
        return resource;
    }
}
