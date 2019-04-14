package com.mmall.config;

import org.springframework.lang.Nullable;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class MvcWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet());
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("*.do", "/");
    }

    @Nullable
    @Override
    protected Class<?>[] getRootConfigClasses() {

        return new Class[]{RootConfig.class};//初始化spring的根容器
        //RootConfig.class相当于平常配置的applicationContext.xml文件
    }

    @Nullable
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]
                {SpringMvcConfig.class};//初始化springMVC的子容器
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};//springMVC匹配的路径
    }
}
