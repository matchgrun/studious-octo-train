/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.tree;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Match Grun
 *
 */
public abstract class BaseTestSingleParentTree {

    private static final Logger logger = LoggerFactory.getLogger( BaseTestSingleParentTree.class );

    /**
     * Dump the tree for debug.
     * @param tree Tree.
     */
    protected void dumpTree( final SingleParentTree tree ) {
        if( logger.isDebugEnabled() ) {
            if( tree == null ) {
                logger.debug( "tree IS-NULL" );
            }
            else {
                logger.debug( "tree:\n" + tree );
            }
        }
    }

    protected void dumpNodes( final List<SingleParentNode> listNodes ) {
        if( logger.isDebugEnabled() ) {
            if( listNodes == null ) {
                logger.debug( "listNodes IS-NULL" );
            }
            else {
                listNodes.forEach( node -> {
                    logger.debug( "node: " + node );
                });
            }
        }
    }

    /**
     * <p>
     * Build a simple tree that should not fail. The tree has the following
     * structure:
     * </p>
     *
     * <table border="2" cellpadding="4">
     * <tr><td bgcolor="#d0d0d0">
     * <pre>
     * Node-100
     *    |
     *    +---- Node-110
     *    |
     *    +---- Node-120
     *    |
     *    +---- Node-130
     *    |        |
     *    |        +---- Node-131
     *    |        |
     *    |        +---- Node-132
     *    |
     *    +---- Node-140
     * </pre>
     * </td></tr>
     * </table>
     *
     * @return Tree.
     * @exception NodeCreationException
     *                if error creating node.
     * @exception CyclicLoopException
     *                if attempting to create tree with cyclic error.
     * @exception DuplicateNodeException
     *                if attempting to create duplicate nodes.
     */
    protected SingleParentTree buildTree()
    throws NodeCreationException, CyclicLoopException, DuplicateNodeException
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "buildTree()");
        }
       SingleParentTree tree = new SingleParentTree();
       SingleParentNode nodeRoot;

       nodeRoot = tree.createNode( "100", "Node-100" );
       tree.addNode( nodeRoot );

       SingleParentNode node110 = tree.createNode( "110", "Node-110" );
       tree.addChild( nodeRoot, node110 );

       SingleParentNode node120 = tree.createNode( "120", "Node-120" );
       tree.addChild( nodeRoot, node120 );

       SingleParentNode node130 = tree.createNode( "130", "Node-130" );
       tree.addChild( nodeRoot, node130 );

       SingleParentNode node140 = tree.createNode( "140", "Node-140" );
       tree.addChild( nodeRoot, node140 );

       SingleParentNode node131 = tree.createNode( "131", "Node-131" );
       tree.addChild( node130, node131 );

       SingleParentNode node132 = tree.createNode( "132", "Node-132" );
       tree.addChild( node130, node132 );

       if( logger.isDebugEnabled() ) {
           logger.debug( "buildTree()...done" );
       }

       return tree;
    }

    /**
     * <p>
     * Build a complex tree that should not fail. The tree has the following
     * structure:
     * </p>
     *
     * <table border="2" cellpadding="4">
     * <tr><td bgcolor="#d0d0d0">
     * <pre>
     * Node-100
     *    |
     *    +---- Node-110
     *    |
     *    +---- Node-120
     *    |
     *    +---- Node-130
     *    |        |
     *    |        +---- Node-131
     *    |        |
     *    |        +---- Node-132
     *    |
     *    +---- Node-140
     *
     * Node-200
     *    |
     *    +---- Node-210
     *    |
     *    +---- Node-220
     *    |
     *    +---- Node-230
     * </pre>
     * </td></tr>
     * </table>
     *
     * @return Tree.
     * @exception NodeCreationException
     *                if error creating node.
     * @exception CyclicLoopException
     *                if attempting to create tree with cyclic error.
     * @exception DuplicateNodeException
     *                if attempting to create duplicate nodes.
     */
    protected SingleParentTree buildTreeComplex()
    throws NodeCreationException, CyclicLoopException, DuplicateNodeException
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "buildTreeComplex()");
        }

       SingleParentTree tree = new SingleParentTree();
       SingleParentNode nodeRoot;

       nodeRoot = tree.createNode( "100", "Node-100" );
       tree.addNode( nodeRoot );

       SingleParentNode node110 = tree.createNode( "110", "Node-110" );
       tree.addChild( nodeRoot, node110 );

       SingleParentNode node120 = tree.createNode( "120", "Node-120" );
       tree.addChild( nodeRoot, node120 );

       SingleParentNode node130 = tree.createNode( "130", "Node-130" );
       tree.addChild( nodeRoot, node130 );

       SingleParentNode node140 = tree.createNode( "140", "Node-140" );
       tree.addChild( nodeRoot, node140 );

       SingleParentNode node131 = tree.createNode( "131", "Node-131" );
       tree.addChild( node130, node131 );

       SingleParentNode node132 = tree.createNode( "132", "Node-132" );
       tree.addChild( node130, node132 );

       nodeRoot = tree.createNode( "200", "Node-200" );
       tree.addNode( nodeRoot );

       SingleParentNode node210 = tree.createNode( "210", "Node-210" );
       tree.addChild( nodeRoot, node210 );

       SingleParentNode node220 = tree.createNode( "220", "Node-220" );
       tree.addChild( nodeRoot, node220 );

       SingleParentNode node230 = tree.createNode( "230", "Node-230" );
       tree.addChild( nodeRoot, node230 );

       if( logger.isDebugEnabled() ) {
           logger.debug( "buildTreeComplex()...done" );
       }

       return tree;
    }

    /**
     * <p>
     * Build a simple tree by specifying the node ID's of related parent-child
     * members. The tree has the following structure:
     * </p>
     *
     * <table border="2" cellpadding="4">
     * <tr><td bgcolor="#d0d0d0">
     * <pre>
     * Node-100
     *    |
     *    +---- Node-110
     *    |
     *    +---- Node-120
     *    |
     *    +---- Node-130
     *    |        |
     *    |        +---- Node-131
     *    |        |
     *    |        +---- Node-132
     *    |
     *    +---- Node-140
     * </pre>
     * </td></tr>
     * </table>
     *
     * @return Tree.
     * @exception NodeCreationException
     *                if error creating node.
     * @exception CyclicLoopException
     *                if attempting to create tree with cyclic error.
     * @exception DuplicateNodeException
     *                if attempting to create duplicate nodes.
     */
    protected SingleParentTree buildTreePairs()
    throws NodeCreationException, CyclicLoopException, DuplicateNodeException
    {
        if( logger.isDebugEnabled() ) {
            logger.debug( "buildTreePairs()" );
        }
       SingleParentTree tree = new SingleParentTree();
       tree.addParentChild( "100", "110" );
       tree.addParentChild( "100", "120" );
       tree.addParentChild( "100", "130" );
       tree.addParentChild( "100", "140" );
       tree.addParentChild( "130", "131" );
       tree.addParentChild( "130", "132" );

       if( logger.isDebugEnabled() ) {
           logger.debug( "buildTreePairs()...done" );
       }

       return tree;
    }


}
