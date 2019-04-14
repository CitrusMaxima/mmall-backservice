package com.mmall.config;

import com.mmall.controller.common.interceptor.AuthorityInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.LinkedList;
import java.util.List;

@Configuration
//@ImportResource(locations = {"classpath:/applicationContext.xml"})
@ComponentScan(basePackages = {"com.mmall.controller", "com.mmall.config"}, includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)})
public class SpringMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> pathPatternsList = new LinkedList<>();
        pathPatternsList.add("/manage/**");
        registry.addInterceptor(new AuthorityInterceptor()).addPathPatterns(pathPatternsList);
    }

    @Bean
    public StringHttpMessageConverter getStringHttpMessageConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter();
        List<String> typeList = new LinkedList<>();
        typeList.add("text/plain;charset=UTF-8");
        typeList.add("text/html;charset=UTF-8");
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes(typeList));

        return converter;
    }

    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<String> typeList = new LinkedList<>();
        typeList.add("application/json;charset=UTF-8");
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes(typeList));

        return converter;
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver getCommonsMultipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(10485760);
        resolver.setMaxInMemorySize(4096);
        resolver.setDefaultEncoding("UTF-8");

        return resolver;
    }
}
