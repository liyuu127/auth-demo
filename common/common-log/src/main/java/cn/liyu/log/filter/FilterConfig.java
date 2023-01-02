package cn.liyu.log.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author liyu
 * date 2021/1/22 15:07
 * description
 */
@Configuration
public class FilterConfig {

    /**
     * request log打印排除url列表
     */
    private static final String[] EXCLUDED_REQUEST_LOG_PRINT = {
            "/websocket/*",
            "/progress/record/export",
    };

    @Autowired
    LoggingFilter loggingFilter;

    @Bean
    public FilterRegistrationBean<LoggingFilter> filterRegistrationBean() {
        loggingFilter.initExcludedUriRepresentation(EXCLUDED_REQUEST_LOG_PRINT);

        FilterRegistrationBean<LoggingFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setOrder(Integer.MIN_VALUE);
        filterFilterRegistrationBean.setFilter(loggingFilter);
        filterFilterRegistrationBean.setUrlPatterns(new ArrayList<>(Arrays.asList("/*")));
        return filterFilterRegistrationBean;
    }

}
