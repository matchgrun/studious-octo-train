/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class TestSingleParentTree02
extends BaseTestSingleParentTree
{

    private static final Logger logger = LoggerFactory.getLogger( TestSingleParentTree02.class );

    private static final String LINE_SEP = "-----------------------------------------------------------";

    /**
     * Exercise simple tree that loads and and fetches all tree nodes.
     */
    @Test
    public void testComplexTree01_Load() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testComplexTree01_Load()");
            logger.debug( LINE_SEP);
        }
        SingleParentTree tree = null;
        try {
            tree = buildTreeComplex();
        }
        catch( Exception ex ) {
            fail( "Test Failed" );
        }

        dumpTree( tree );
        assertNotNull( tree, "Tree is empty" );

        String strFind100 = "100";
        if( logger.isDebugEnabled() ) {
            logger.debug( "Parent Node to find: " + strFind100 );
        }
        String strFind200 = "200";
        if( logger.isDebugEnabled() ) {
            logger.debug( "Parent Node to find: " + strFind200 );
        }

        SingleParentNode nodeFind100 = tree.fetchNode( strFind100 );
        if( logger.isDebugEnabled() ) {
            logger.debug( "found: " + nodeFind100 );
        }
        assertNotNull( nodeFind100, "Could not find node" );

        SingleParentNode nodeFind200 = tree.fetchNode( strFind200 );
        if( logger.isDebugEnabled() ) {
            logger.debug( "found: " + nodeFind200 );
        }
        assertNotNull( nodeFind200, "Could not find node" );

        if( logger.isDebugEnabled() ) {
            logger.debug( "Root nodes:" );
        }

        List<SingleParentNode> listRootNodes = tree.fetchAllRootNodes();
        dumpNodes( listRootNodes );
        assertNotNull( listRootNodes, "fetchAllRootNodes failed" );

        if( logger.isDebugEnabled() ) {
            logger.debug( "Number of nodes=" + listRootNodes.size() );
        }
        assertEquals( 2, listRootNodes.size(), "Too few nodes" );
    }

    /**
     * Exercise pruning of tree.
     */
    @Test
    public void testComplexTree02_Prune() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testComplexTree02_Prune()");
            logger.debug( LINE_SEP);
        }
        SingleParentTree tree = null;
        try {
            tree = buildTreeComplex();
        }
        catch( Exception ex ) {
            fail( "Test Failed" );
        }

        if( logger.isDebugEnabled() ) {
            logger.debug( "Tree before:" );
        }
        dumpTree( tree );
        assertNotNull( tree, "Tree is empty" );

        String strFind130 = "130";
        if( logger.isDebugEnabled() ) {
            logger.debug( "Parent Node to find: " + strFind130 );
        }
        SingleParentNode node130 = tree.fetchNode( strFind130 );
        if( logger.isDebugEnabled() ) {
            logger.debug( "found: " + node130 );
        }
        assertNotNull( node130, "Could not find node" );

        if( logger.isDebugEnabled() ) {
            logger.debug( "Nodes before:" );
        }

        List<SingleParentNode> listNodes = tree.fetchAllNodes();
        dumpNodes( listNodes );
        assertNotNull( listNodes, "fetchAllNodes failed" );

        int countBefore = listNodes.size();
        if( logger.isDebugEnabled() ) {
            logger.debug( "countBefore=" + countBefore );
        }
        assertEquals( 11, listNodes.size(), "Too few nodes" );

        if( logger.isDebugEnabled() ) {
            logger.debug( "Pruning tree..." );
        }
        tree.pruneTree( node130 );

        if( logger.isDebugEnabled() ) {
            logger.debug( "Tree after:" );
        }
        dumpTree( tree );

        if( logger.isDebugEnabled() ) {
            logger.debug( "Nodes after:");
        }

        listNodes = tree.fetchAllNodes();
        dumpNodes( listNodes );
        assertNotNull( listNodes, "fetchAllNodes failed" );

        int countAfter = listNodes.size();
        if( logger.isDebugEnabled() ) {
            logger.debug( "countAfter=" + countAfter );
        }
        assertEquals( countBefore - 3, listNodes.size(), "Too few nodes" );
    }

    /**
     * Exercise deletion of single node from tree.
     */
    @Test
    public void testComplexTree03_DeleteNode() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testComplexTree03_DeleteNode()");
            logger.debug( LINE_SEP);
        }
        SingleParentTree tree = null;
        try {
            tree = buildTreeComplex();
        }
        catch( Exception ex ) {
            fail( "Test Failed" );
        }

        if( logger.isDebugEnabled() ) {
            logger.debug( "Tree before:" );
        }
        dumpTree( tree );
        assertNotNull( tree, "Tree is empty" );

        String strFind130 = "130";
        if( logger.isDebugEnabled() ) {
            logger.debug( "Parent Node to find: " + strFind130 );
        }
        SingleParentNode node130 = tree.fetchNode( strFind130 );
        if( logger.isDebugEnabled() ) {
            logger.debug( "found: " + node130 );
        }
        assertNotNull( node130, "Could not find node" );

        if( logger.isDebugEnabled() ) {
            logger.debug( "Root Nodes before:" );
        }

        List<SingleParentNode> listRootNodes = tree.fetchAllRootNodes();
        dumpNodes( listRootNodes );

        int countBefore = listRootNodes.size();
        if( logger.isDebugEnabled() ) {
            logger.debug( "countBefore=" + countBefore );
        }

        if( logger.isDebugEnabled() ) {
            logger.debug( "Delete node from tree..." );
        }

        tree.delete( node130 );

        if( logger.isDebugEnabled() ) {
            logger.debug( "Tree after:" );
        }
        dumpTree( tree );

        if( logger.isDebugEnabled() ) {
            logger.debug( "Root Nodes after:" );
        }
        listRootNodes = tree.fetchAllRootNodes();
        dumpNodes( listRootNodes );
    }


}
