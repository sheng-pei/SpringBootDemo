package org.example.web;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class Config {
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    public ServletRegistrationBean forwardServlet() {
        AnnotationConfigWebApplicationContext applicationContext
                = new AnnotationConfigWebApplicationContext();
        applicationContext.scan("forward");
        applicationContext.refresh();
        DispatcherServlet forwardDispatcherServlet
                = new DispatcherServlet(applicationContext);

        ServletRegistrationBean registrationBean
                = new ServletRegistrationBean(forwardDispatcherServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/forward/*");
        registrationBean.setName("forwardDispatcherServlet");
        return registrationBean;
    }

    //    @Bean
//    public ServiceLogWriter serviceLogWriter() {
//        return new ServiceLogWriter();
//    }
//
//    @Bean
//    public DisableGlobalExceptionHandler disableGlobalExceptionHandler() {
//        return new DisableGlobalExceptionHandler();
//    }

//    @Bean
//    public SlowServiceWatcher slowServiceWatcher() {
//        return new SlowServiceWatcher();
//    }

//    @Bean
//    public OKResponse okResponse() {
//        return new OKResponse();
//    }

//    @Bean
//    public BeforeResponse beforeResponse() {
//        return new BeforeResponse();
//    }
}
