package net.therap.leavemanagement.configuration;

import net.therap.leavemanagement.filter.ExceptionFilter;
import net.therap.leavemanagement.filter.InvalidSessionFilter;
import net.therap.leavemanagement.filter.UserActivationFilter;
import net.therap.leavemanagement.filter.ValidSessionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rumi.dipto
 * @since 1/19/22
 */
@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean<InvalidSessionFilter> invalidSessionFilter() {
        FilterRegistrationBean<InvalidSessionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new InvalidSessionFilter());
        registrationBean.addUrlPatterns(
                "/dashboard",
                "/user/*",
                "/notification",
                "/leave/*",
                "/logout",
                "/success"
        );

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ValidSessionFilter> validSessionFilter() {
        FilterRegistrationBean<ValidSessionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ValidSessionFilter());
        registrationBean.addUrlPatterns("/", "/login");

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<UserActivationFilter> userActivationFilter() {
        FilterRegistrationBean<UserActivationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UserActivationFilter());
        registrationBean.addUrlPatterns(
                "/dashboard",
                "/user/teamLeadList",
                "/user/developerList",
                "/user/testerList",
                "/user/details",
                "/user/form",
                "/user/submit",
                "/notification",
                "/leave/*",
                "/logout",
                "/success"
        );

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ExceptionFilter> exceptionFilter() {
        FilterRegistrationBean<ExceptionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ExceptionFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
