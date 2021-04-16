/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.tree;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Match Grun
 *
 */
public class TestSingleParentNode {

    private static final Logger logger = LoggerFactory.getLogger( TestSingleParentNode.class );

    private static final int TEST_LEVEL = 1;
    private static final int TEST_SEQUENCE = 10;

    private static final String TEST_ID = "testID";
    private static final String TEST_VALUE = "testValue";

    @Test
    public void testSingleParentNode01() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testSingleParentNode01()");
        }

        SingleParentNode node = new SingleParentNode();
        node.setID( TEST_ID );
        node.setText( TEST_VALUE );
        node.setLevel( TEST_LEVEL );
        node.setSequence( TEST_SEQUENCE );

        if( logger.isDebugEnabled() ) {
            logger.debug( "node=" + node );
        }
    }
}
