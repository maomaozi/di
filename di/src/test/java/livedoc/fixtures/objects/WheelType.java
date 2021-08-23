package livedoc.fixtures.objects;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface WheelType {
    Type type() default Type.SMALL;

    enum Type {
        BIG, SMALL, MEDIUM
    }
}
