/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.util;

import java.util.HashSet;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.ibm.wh.siam.core.common.CommonConstants;

/**
 * @author Match Grun
 *
 */
public class StatusUtil {

    private static Set<String> SET_STATUS = buildStatus();
    private static String VALUES_ARE = buildValuesAre();

    private static Set<String> buildStatus() {
        Set<String> set = new HashSet<String>();
        set.add(CommonConstants.STATUS_ACTIVE.toUpperCase());
        set.add(CommonConstants.STATUS_INACTIVE.toUpperCase());
        return set;
    }

    private static String buildValuesAre() {
        return SET_STATUS.toString();
    }

    public static boolean isValid( final String sts ) {
        if( sts == null ) return false;
        return SET_STATUS.contains(sts.toUpperCase());
    }

    public static String defaultValue( final String sts ) {
        if( StringUtils.isEmpty(sts) ) {
            return CommonConstants.STATUS_ACTIVE;
        }
        return sts.toUpperCase();
    }

    public static String valuesAre() {
        return VALUES_ARE;
    }
}
