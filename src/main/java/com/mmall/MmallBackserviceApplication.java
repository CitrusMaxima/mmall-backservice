package com.mmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
//开启自动配置
@EnableAutoConfiguration
//开启热刷新配置
@RefreshScope
//作为eureka客户端
@EnableEurekaClient
@Controller
public class MmallBackserviceApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MmallBackserviceApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MmallBackserviceApplication.class, args);
    }

    @RequestMapping(path = "/")
    public String getIndex() {
        return "/WEB-INF/index.html";
    }
}

