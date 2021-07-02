package com.alconn.copang.annotations;

import com.alconn.copang.client.Role;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Retention(RetentionPolicy.RUNTIME)
@IdentitySecured
@NotNull
@Validated
@Target(ElementType.PARAMETER)
public @interface InjectId {

    Role role() default Role.CLIENT;
}
