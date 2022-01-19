package net.therap.leavemanagement.configuration;

import net.therap.leavemanagement.formatter.UserFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author rumi.dipto
 * @since 1/19/22
 */
@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/styles/*").addResourceLocations("/styles/");
        registry.addResourceHandler("/js/*").addResourceLocations("/js/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new UserFormatter());
    }

//    @Bean
//    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
//        return new OpenEntityManagerInViewInterceptor();
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor());
//    }
}
