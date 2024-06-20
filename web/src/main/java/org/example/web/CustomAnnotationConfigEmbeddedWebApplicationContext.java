//package org.example.web;
//
//import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletContextInitializer;
//
//import java.util.Arrays;
//import java.util.Collection;
//
//public class CustomAnnotationConfigEmbeddedWebApplicationContext extends AnnotationConfigEmbeddedWebApplicationContext {
//    @Override
//    protected Collection<ServletContextInitializer> getServletContextInitializerBeans() {
//        Collection<ServletContextInitializer> initializers = super.getServletContextInitializerBeans();
//        for (ServletContextInitializer initializer : initializers) {
//            if (initializer instanceof FilterRegistrationBean) {
//                FilterRegistrationBean bean = (FilterRegistrationBean) initializer;
//                Collection<String> urlPatterns = bean.getUrlPatterns();
//                if (urlPatterns == null || urlPatterns.isEmpty()) {
//                    bean.setUrlPatterns(Arrays.asList("/rest/*"));
//                }
//            }
//        }
//        return initializers;
//    }
//}
