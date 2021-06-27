package com.alconn.copang.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@IdentitySecured
@Target(ElementType.PARAMETER)
public @interface InjectId {
}
