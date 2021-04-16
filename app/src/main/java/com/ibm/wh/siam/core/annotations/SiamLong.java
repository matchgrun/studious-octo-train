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
public @interface SiamLong {
    String fieldName() default "";
    boolean empty() default true;
    long minValue() default -1L;
    long maxValue() default 1L;
    boolean range() default false;
}
