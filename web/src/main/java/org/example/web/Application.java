package org.example.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(Application.class);
//        application.setApplicationContextClass(CustomAnnotationConfigEmbeddedWebApplicationContext.class);
        ApplicationContext context = application.run(args);
    }
}
