/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.core.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.wh.siam.core.common.CommonConstants;

/**
 * @author Match Grun
 *
 */
public class TestAssignmentUtil {
    private static final Logger logger = LoggerFactory.getLogger( TestAssignmentUtil.class );

    @Test
    public void testUtil01() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testUtil01()");
        }

        assertTrue( AssignmentUtil.isValid(CommonConstants.OPERATION_ASSIGN ), "Failed test assignment: OPERATION_ASSIGN" );
        assertTrue( AssignmentUtil.isValid(CommonConstants.OPERATION_UNASSIGN ), "Failed test assignment: OPERATION_UNASSIGN" );

        assertFalse( AssignmentUtil.isValid( "JUNK" ), "Failed test for invalid assignment." );
    }

    @Test
    public void testUtil02() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testUtil02()");
        }

        String assignment = CommonConstants.OPERATION_ASSIGN.toUpperCase();
        String str = AssignmentUtil.defaultValue( assignment );
        if( logger.isDebugEnabled() ) {
            logger.debug( "assignment=" + assignment );
            logger.debug( "str=" + str );
        }
        assertTrue( str.equalsIgnoreCase( assignment ), "Failed return defaultValue for: " + assignment );

        assignment = CommonConstants.OPERATION_ASSIGN.toUpperCase();
        str = AssignmentUtil.defaultValue( assignment );
        if( logger.isDebugEnabled() ) {
            logger.debug( "assignment=" + assignment );
            logger.debug( "str=" + str );
        }
        assertTrue( str.equalsIgnoreCase( assignment ), "Failed return defaultValue for: " + assignment );
    }

}
