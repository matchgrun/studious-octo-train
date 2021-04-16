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
public @interface SiamInteger {
    String fieldName() default "";
    boolean empty() default true;
    int minValue() default -1;
    int maxValue() default 1;
    boolean range() default false;

//    boolean positive() default true;
//    boolean negative() default true;
//    boolean nonZero() default true;
}
