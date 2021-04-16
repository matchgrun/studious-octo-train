/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.tree;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Match Grun
 *
 */
public class TestSingleParentTree03
extends BaseTestSingleParentTree
{

    private static final Logger logger = LoggerFactory.getLogger( TestSingleParentTree03.class );

    private static final String LINE_SEP = "-----------------------------------------------------------";

    /**
     * Exercise simple tree that loads and and fetches all tree nodes.
     */
    @Test
    public void testTreePairs01() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testTreePairs01()");
            logger.debug( LINE_SEP);
        }
        SingleParentTree tree = null;
        try {
            tree = buildTreePairs();
        }
        catch( Exception ex ) {
            fail( "Test Failed" );
        }

        dumpTree( tree );
        assertNotNull( tree, "Tree is empty" );

        String strFind = "100";
        if( logger.isDebugEnabled() ) {
            logger.debug( "Parent Node to find: " + strFind  );
        }

        SingleParentNode nodeFind = tree.fetchNode( strFind  );
        if( logger.isDebugEnabled() ) {
            logger.debug( "found: " + nodeFind );
        }
        assertNotNull( nodeFind, "Could not find node" );

        if( logger.isDebugEnabled() ) {
            logger.debug( "Node list:" );
        }

        List<SingleParentNode> listNodes = tree.fetchAllNodes( nodeFind );
        dumpNodes( listNodes );
        assertNotNull( listNodes, "fetchAllNodes failed" );

        if( logger.isDebugEnabled() ) {
            logger.debug( "Number of nodes=" + listNodes.size() );
        }
    }

    /**
     * Exercise simple tree by attempting to insert a duplicate node.
     */
    @Test
    public void testTreePairs02() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testTreePairs02()");
            logger.debug( LINE_SEP);
        }
        SingleParentTree tree = null;
        try {
           tree = buildTreePairs();
        }
        catch( Exception ex ) {
           fail( "Test Failed" );
        }

        dumpTree( tree );
        assertNotNull( tree, "Tree is empty" );

        String strFind = "100";
        if( logger.isDebugEnabled() ) {
            logger.debug( "Parent Node to find: " + strFind  );
        }

        SingleParentNode node100 = tree.fetchNode( strFind  );
        if( logger.isDebugEnabled() ) {
            logger.debug( "found: " + node100 );
        }
        assertNotNull( node100, "Could not find node" );

        String strDuplicate = "140";
        if( logger.isDebugEnabled() ) {
            logger.debug( "Node to duplicate: " + strDuplicate  );
        }

        SingleParentNode node140 = tree.fetchNode( strDuplicate );
        assertNotNull( node140, "Could not find node" );
        if( logger.isDebugEnabled() ) {
            logger.debug( "found: " + node140 );
        }

        try {
           tree.addChild( node100, node140 );
        }
        catch( Exception ex ) {
            if( logger.isDebugEnabled() ) {
                logger.debug( "Duplicate caught" );
            }
        }
        dumpTree( tree );
    }


}
