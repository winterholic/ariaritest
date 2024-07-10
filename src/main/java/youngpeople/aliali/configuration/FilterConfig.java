package youngpeople.aliali.configuration;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import youngpeople.aliali.filter.MyAuthenticationFilter;
import youngpeople.aliali.manager.TokenManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final TokenManager tokenManager;

    @Bean
    public FilterRegistrationBean addAuthenticationFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        filterRegistrationBean.setFilter(new MyAuthenticationFilter(tokenManager));
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

}
