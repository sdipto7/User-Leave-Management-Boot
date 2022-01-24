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

    private final String[] invalidSessionFilterUrlPatterns = {"/dashboard", "/user/*", "/notification",
            "/leave/*", "/logout", "/success"};

    private final String[] validSessionFilterUrlPatterns = {"/", "/login"};

    private final String[] userActivationFilterUrlPatterns = {"/dashboard", "/user/teamLeadList",
            "/user/developerList", "/user/testerList", "/user/details", "/user/form", "/user/submit",
            "/notification", "/leave/*", "/logout", "/success"};

    private final String[] exceptionUrlPatterns = {"/*"};

    @Bean
    public FilterRegistrationBean<InvalidSessionFilter> invalidSessionFilter() {
        FilterRegistrationBean<InvalidSessionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new InvalidSessionFilter());
        registrationBean.addUrlPatterns(invalidSessionFilterUrlPatterns);
        registrationBean.setOrder(1);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ValidSessionFilter> validSessionFilter() {
        FilterRegistrationBean<ValidSessionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ValidSessionFilter());
        registrationBean.addUrlPatterns(validSessionFilterUrlPatterns);
        registrationBean.setOrder(2);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<UserActivationFilter> userActivationFilter() {
        FilterRegistrationBean<UserActivationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UserActivationFilter());
        registrationBean.addUrlPatterns(userActivationFilterUrlPatterns);
        registrationBean.setOrder(3);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ExceptionFilter> exceptionFilter() {
        FilterRegistrationBean<ExceptionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ExceptionFilter());
        registrationBean.addUrlPatterns(exceptionUrlPatterns);
        registrationBean.setOrder(4);

        return registrationBean;
    }
}
