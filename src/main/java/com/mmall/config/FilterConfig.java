package com.mmall.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.LinkedList;
import java.util.List;

@Configuration
@ComponentScan()
public class FilterConfig {
    @Bean
    public FilterRegistrationBean getCharacterEncodingFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        registrationBean.setFilter(filter);
        List<String> urlPatternsList = new LinkedList<>();
        urlPatternsList.add("/*");
        registrationBean.setUrlPatterns(urlPatternsList);
        registrationBean.setName("characterEncodingFilter");

        return registrationBean;
    }

/*    @Bean
    public DelegatingFilterProxyRegistrationBean getSpringSessionRepositoryFilter(){
        DelegatingFilterProxyRegistrationBean registrationBean = new DelegatingFilterProxyRegistrationBean("delegatingFilterProxy");
        List<String> urlPatternsList = new LinkedList<>();
        urlPatternsList.add("*.do");
        registrationBean.setUrlPatterns(urlPatternsList);
        registrationBean.setName("springSessionRepositoryFilte");

        return registrationBean;
    }

    @Bean(name = "delegatingFilterProxy")
    public DelegatingFilterProxy getDelegatingFilterProxy(){
        DelegatingFilterProxy filterProxy = new DelegatingFilterProxy();
        return filterProxy;
    }*/
}
