package optionsParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by erick on 09/07/16.
 */

// NOTE(erick): RetentionPolicy.RUNTIME is need so we can read this annotation in runtime (using reflection)
//    Reference: https://docs.oracle.com/javase/7/docs/api/java/lang/annotation/RetentionPolicy.html
@Retention(RetentionPolicy.RUNTIME)

@Target(ElementType.FIELD)

public @interface Option {
    String name() default "";
    String defaultValue() default "";
    boolean isRequired() default false;
}
