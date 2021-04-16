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
public class TestSingleParentTree01
extends BaseTestSingleParentTree
{

    private static final Logger logger = LoggerFactory.getLogger( TestSingleParentTree01.class );

    private static final String LINE_SEP = "-----------------------------------------------------------";

    /**
     * Exercise simple tree that loads and and fetches all tree nodes.
     */
    @Test
    public void testSimpleTree01() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testSimpleTree01()");
            logger.debug( LINE_SEP);
        }
        SingleParentTree tree = null;
        try {
            tree = buildTree();
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
     * Exercise simple tree by retrieving ancestry for a node.
     */
    @Test
    public void testSimpleTree02() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testSimpleTree02()");
            logger.debug( LINE_SEP);
        }

        SingleParentTree tree = null;
        try {
            tree = buildTree();
        }
        catch( Exception ex ) {
            fail( "Test Failed" );
        }

        dumpTree( tree );
        assertNotNull( tree, "Tree is empty" );

        String strFind = "131";
        if( logger.isDebugEnabled() ) {
           logger.debug( "Parent Node to find: " + strFind  );
        }

        SingleParentNode nodeFind = tree.fetchNode( strFind  );
        if( logger.isDebugEnabled() ) {
            logger.debug( "found: " + nodeFind );
        }
        assertNotNull( nodeFind, "Could not find node" );

        if( logger.isDebugEnabled() ) {
            logger.debug( "Ancestry:" );
        }
        List<SingleParentNode> listAncesters = nodeFind.ancestry();
        dumpNodes( listAncesters );
        assertNotNull( listAncesters, "fetchAllNodes failed" );

        if( logger.isDebugEnabled() ) {
            logger.debug( "Number of ancestors=" + listAncesters.size() );
        }
        assertEquals( 2, listAncesters.size(), "Too few nodes" );
    }

    /**
     * Exercise simple tree by retrieving a sub-tree.
     */
    @Test
    public void testSimpleTree03_SubTree() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testSimpleTree03_SubTree()");
            logger.debug( LINE_SEP);
        }
        SingleParentTree tree = null;
        try {
           tree = buildTree();
        }
        catch( Exception ex ) {
           fail( "Test Failed" );
        }

        dumpTree( tree );
        assertNotNull( tree, "Tree is empty" );

        String strFind = "130";
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

        if( logger.isDebugEnabled() ) {
            logger.debug( "Number of nodes=" + listNodes.size() );
        }
        assertNotNull( listNodes, "fetchAllNodes failed" );
        assertEquals( 3, listNodes.size(), "Too few nodes" );
    }

    /**
     * Exercise simple tree by attempting to insert a duplicate node.
     */
    @Test
    public void testSimpleTree04_Duplicate() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testSimpleTree04_Duplicate()");
            logger.debug( LINE_SEP);
        }
        SingleParentTree tree = null;
        try {
           tree = buildTree();
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
                logger.debug( "Duplicate caught: " + ex.getMessage() );
            }
        }
        dumpTree( tree );
    }

    /**
     * Exercise simple tree by attempting to re-parent node.
     * Node 131 should be re-parented from 130 to 140
     */
    @Test
    public void testSimpleTree05_Reparent() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testSimpleTree05_Reparent()");
            logger.debug( LINE_SEP);
        }
        SingleParentTree tree = null;
        try {
           tree = buildTree();
        }
        catch( Exception ex ) {
           fail( "Test Failed" );
        }

        if( logger.isDebugEnabled() ) {
            logger.debug( "Tree before:");
        }

        dumpTree( tree );
        assertNotNull( tree, "Tree is empty" );

        String strFind131 = "131";
        if( logger.isDebugEnabled() ) {
            logger.debug( "Node to test as child:" + strFind131  );
        }

        SingleParentNode node131 = tree.fetchNode( strFind131 );
        if( logger.isDebugEnabled() ) {
            logger.debug( "node131: " + node131 );
        }
        assertNotNull( node131, "Could not find node" );

        SingleParentNode oldParent = node131.getParentNode();
        if( logger.isDebugEnabled() ) {
            logger.debug( "oldParent: " + oldParent );
        }
        assertNotNull( oldParent, "Node has no parent" );

        if( logger.isDebugEnabled() ) {
            logger.debug( "Node to test as parent:" );
        }

        String strFind140 = "140";
        SingleParentNode node140 = tree.fetchNode( strFind140 );
        if( logger.isDebugEnabled() ) {
            logger.debug( "node140: " + node140 );
        }
        assertNotNull( node140, "Could not find node"  );

        try {
            tree.makeParent( strFind140, strFind131 );
        }
        catch( Exception ex ) {
            if( logger.isDebugEnabled() ) {
                logger.debug( "Re-parent node test failed miserably" );
                logger.debug( ex.getMessage() );
            }
            fail( "Re-parent node test failed miserably"  );
        }

        if( logger.isDebugEnabled() ) {
            logger.debug( "Tree after:" );
        }
        dumpTree( tree );

        assertEquals(
               node140.getID(),
               node131.getParentNode().getID(),
               "Parent node ID incorrect" );

        assertEquals(
               1,
               oldParent.countChildren(),
               "Old parent should have no children" );
    }

    /**
     * Exercise simple tree by attempting to reparent node testing for cyclic
     * loop error.
     */
    @Test
    public void testSimpleTree06_Cyclic() {
        if( logger.isDebugEnabled() ) {
            logger.debug( "testSimpleTree06_Cyclic()");
            logger.debug( LINE_SEP);
        }
        SingleParentTree tree = null;
        try {
           tree = buildTree();
        }
        catch( Exception ex ) {
           fail( "Test Failed" );
        }

        if( logger.isDebugEnabled() ) {
            logger.debug( "Tree before:");
        }
        dumpTree( tree );
        assertNotNull( tree, "Tree is empty" );

        String strFind100 = "100";
        if( logger.isDebugEnabled() ) {
            logger.debug( "Node to test as child:" + strFind100 );
        }

        SingleParentNode node100 = tree.fetchNode( strFind100 );
        assertNotNull( node100, "Could not find node" );
        if( logger.isDebugEnabled() ) {
            logger.debug( "node100: " + node100 );
        }

        if( logger.isDebugEnabled() ) {
            logger.debug( "Node to test as parent:" );
        }

        String strFind131 = "131";
        if( logger.isDebugEnabled() ) {
            logger.debug( "Node to test as child:" + strFind131  );
        }

        SingleParentNode node131 = tree.fetchNode( "131" );
        if( logger.isDebugEnabled() ) {
            logger.debug( "node131: " + node131 );
        }

        try {
            tree.makeParent( strFind131, strFind100 );
            if( logger.isDebugEnabled() ) {
                logger.debug( "Re-parent into cylic loop failed miserably" );
            }
        }
        catch( Exception ex ) {
            if( logger.isDebugEnabled() ) {
                logger.debug( "Success: Exception: " + ex.getMessage() );
            }
        }

        if( logger.isDebugEnabled() ) {
            logger.debug( "Tree after:" );
        }
        dumpTree( tree );
    }


}
