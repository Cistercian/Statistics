package ru.hd.olaf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Created by d.v.hozyashev on 03.08.2017.
 * Класс конфигурации
 */
@Configuration                      //данный класс является Java Configuration;
@EnableWebMvc                       //разрешает нашему проекту использовать MVC;
@ComponentScan("ru.hd.olaf.mvc")    //говорит, где искать компоненты проекта
public class WebConfig_2 extends WebMvcConfigurerAdapter{

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
}
