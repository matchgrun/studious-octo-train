/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Match Grun
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SiamString {
    String fieldName() default "";
    int minLength() default -1;
    int maxLength() default -1;
    boolean empty() default true;
}
