//package org.example.web;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ConfigurableApplicationContext;
//
//public class CustomSpringApplication extends SpringApplication {
//
//    public CustomSpringApplication(Object... sources) {
//        super(sources);
//    }
//
//    public static ConfigurableApplicationContext run(Object source, String... args) {
//        return run(new Object[] { source }, args);
//    }
//
//    public static ConfigurableApplicationContext run(Object[] sources, String[] args) {
//        CustomSpringApplication custom = new CustomSpringApplication(sources);
//        custom.setApplicationContextClass(CustomAnnotationConfigEmbeddedWebApplicationContext.class);
//        return new CustomSpringApplication(sources).run(args);
//    }
//
//}
