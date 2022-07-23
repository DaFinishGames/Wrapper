package it.dependency.injector.annotations;

import it.dependency.injector.core.DefaultClassInjected;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Injected {
    Class<?> className() default DefaultClassInjected.class;
}
