package io.pavelkoch.eoin.rtm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Controller {
    /**
     * @return The event this controller reacts to
     */
    EventType event();

    /**
     * @return The pattern that is needed to be matched in order to invoke the controller.
     */
    String pattern() default "*";
}
