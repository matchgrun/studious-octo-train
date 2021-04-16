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

/**
 * @author Match Grun
 *
 */
public class TestAttributeUtil {

    private static final Logger logger = LoggerFactory.getLogger( TestAttributeUtil.class );

    @Test
    public void testUtil01() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testUtil01()");
        }

        assertTrue( AttributeUtil.isValid( AttributeUtil.ORGANIZATION ), "Failed test ownerType: ORGANIZATION" );
        assertTrue( AttributeUtil.isValid( AttributeUtil.GROUP ), "Failed test ownerType: GROUP" );
        assertTrue( AttributeUtil.isValid( AttributeUtil.CONTENT_SET ), "Failed test ownerType: CONTENT_SET" );
        assertTrue( AttributeUtil.isValid( AttributeUtil.CONTENT_SET_ACCESS ), "Failed test ownerType: CONTENT_SET_ACCESS" );

        assertFalse( AttributeUtil.isValid( "JUNK" ), "Failed test for invalid ownerType." );
    }
}
