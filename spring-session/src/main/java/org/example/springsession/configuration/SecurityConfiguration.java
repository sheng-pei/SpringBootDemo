//package org.example.springsession.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.ObjectPostProcessor;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.ExceptionTranslationFilter;
//import org.springframework.security.web.context.SecurityContextRepository;
//
//@Configuration
//public class SecurityConfiguration {
//    @Bean
//    SecurityFilterChain web(HttpSecurity http) throws Exception {
//        // @formatter:off
//        http
//                .securityContext(context -> context
//                        .securityContextRepository(securityContextRepository())
//                )
//                .authorizeHttpRequests((authorize) -> authorize
//                        .anyRequest().authenticated()
//                ) // 资源授权检查
//                .exceptionHandling((exceptions) -> exceptions
//                        .withObjectPostProcessor(new ObjectPostProcessor<ExceptionTranslationFilter>() {
//                            @Override
//                            public <O extends ExceptionTranslationFilter> O postProcess(O filter) {
//                                filter.setAuthenticationTrustResolver(new MfaTrustResolver());
//                                return filter;
//                            }
//                        })
//                ); // 异常处理
//        // @formatter:on
//        return http.build();
//    }
//
//    @Bean
//    SecurityContextRepository securityContextRepository() {
//
//    }
//
//    @Bean
//    AuthenticationManager authenticationManager() {
//        return null;
//    }
//
////    @Bean
////    AuthorizationManager<RequestAuthorizationContext> authorizationManager() {
////        return (authentication,
////                context) -> new AuthorizationDecision(authentication.get() instanceof MfaAuthentication);
////    }
//}
