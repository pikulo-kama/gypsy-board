package com.adrabazha.gypsy.board.annotation;

import com.adrabazha.gypsy.board.domain.Role;

import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SharedOrganizationAccess {

    Role[] value();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
