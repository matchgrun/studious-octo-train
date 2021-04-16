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
public class AssignmentUtil {

    private static Set<String> SET_ASSIGNMENT = buildAssignment();

    private static Set<String> buildAssignment() {
        Set<String> set = new HashSet<String>();
        set.add(CommonConstants.OPERATION_ASSIGN.toLowerCase());
        set.add(CommonConstants.OPERATION_UNASSIGN.toLowerCase());
        return set;
    }

    public static boolean isValid( final String operation ) {
        if( operation == null ) return false;
        return SET_ASSIGNMENT.contains(operation.toLowerCase());
    }

    public static String defaultValue( final String operation ) {
        if( StringUtils.isEmpty(operation) ) {
            return null;
        }
        return operation.toLowerCase();
    }

}
