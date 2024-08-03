package org.example.springsession.configuration;

import org.example.springsession.login.*;
import org.example.springsession.login.lock.LoginLockChecker;
import org.example.springsession.login.provider.LockableUserDetailsService;
import org.example.springsession.login.provider.PostAuthenticationChecks;
import org.example.springsession.login.provider.PreAuthenticationChecks;
import org.example.springsession.login.reader.UnauthenticatedUsernamePasswordAuthenticationTokenReaderConfigurer;
import org.example.springsession.login.reader.random.RandomManager;
import org.example.springsession.login.reader.random.RedisRandomManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.session.SessionIdGenerator;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {
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
    @Value("user.login.lock.interval:3")
    private int interval;

    @Value("user.login.max.attempts:3")
    private int maxAttempts;

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, LoginLockChecker lockChecker) throws Exception {
        //登录页，测试用
        http.with(new UnauthenticatedUsernamePasswordAuthenticationTokenReaderConfigurer<>(), withDefaults());
        http.with(new EnhancedLoginConfigurer<>(), h -> {
            h.successHandler(authenticationSuccessHandler(lockChecker));
            h.failureHandler(authenticationFailureHandler(lockChecker));
        });
        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        return http.build();
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler(LoginLockChecker checker) {
        AjaxAuthenticationSuccessHandler success = new AjaxAuthenticationSuccessHandler();
        success.setLockChecker(checker);
        return success;
    }

    private AuthenticationFailureHandler authenticationFailureHandler(LoginLockChecker checker) {
        AjaxAuthenticationFailureHandler failure = new AjaxAuthenticationFailureHandler();
        failure.setLockChecker(checker);
        return failure;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(LoginLockChecker checker) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder());
        provider.setUserDetailsService(userDetailsService(checker));
        provider.setPreAuthenticationChecks(preAuthenticationChecks());
        provider.setPostAuthenticationChecks(postAuthenticationChecks());
        return provider;
    }

    private UserDetailsService userDetailsService(LoginLockChecker checker) {
        LockableUserDetailsService service = new LockableUserDetailsService();
        service.setLockChecker(checker);
        return service;
    }

    @Bean
    public LoginLockChecker loginLockChecker(StringRedisTemplate redisTemplate) {
        LoginLockChecker checker = new LoginLockChecker();
        checker.setRedis(redisTemplate);
        checker.setInterval(interval);
        checker.setMaxAttempts(maxAttempts);
        return checker;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MessageDigestPasswordEncoder("SHA-256");
    }

    @Bean
    public UserDetailsChecker preAuthenticationChecks() {
        return new PreAuthenticationChecks(interval);
    }

    @Bean
    public UserDetailsChecker postAuthenticationChecks() {
        return new PostAuthenticationChecks();
    }

    @Bean
    public RandomManager randomManager(StringRedisTemplate redisTemplate) {
        RedisRandomManager manager = new RedisRandomManager();
        manager.setRedis(redisTemplate);
        return manager;
    }

    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new TGTSessionIdGenerator();
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setCookieName("CASTGC");
        cookieSerializer.setUseBase64Encoding(false);
        return cookieSerializer;
    }

//    @Bean
//    SecurityContextRepository securityContextRepository() {
//
//    }
}
