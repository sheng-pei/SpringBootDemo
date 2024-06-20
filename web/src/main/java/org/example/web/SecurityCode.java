package org.example.web;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SecurityCode {
    String[] codes() default {};

    @AliasFor("value")
    String code() default "";

    @AliasFor("code")
    String value() default "";
}
