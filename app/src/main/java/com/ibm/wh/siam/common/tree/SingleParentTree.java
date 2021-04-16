/**
 *
 * @Author Match Grun.
 */
package com.ibm.wh.siam.common.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <p>
 * Defines a tree comprising a hierarchy of SingleParentNode objects. Each node
 * in the tree may have many parents and many children. The tree manages all
 * nodes that appear in the tree by maintaining an internal hashmap. An example
 * of such a tree is illustrated below.
 * </p>
 *
 * <blockquote>
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
 * </blockquote>
 *
 * <p>
 * Nodes can be inserted into the "tree" in an sequence, and linked to parent
 * and child nodes later. Therefore, the tree can actually have many trees with
 * many root nodes. When nodes are added to the tree, a check is performed to
 * ensure that a node with the same ID does not appear in the parent's ancestry.
 * This will result in an exception; the node will not be inserted.
 * </p>
 *
 * @author Match Grun
 *
 */
public class SingleParentTree
implements java.io.Serializable
{
// -----------------------------------------------------------------------------
// Constants.
// -----------------------------------------------------------------------------
   /**
    * Serialized ID.
    */
    private static final long serialVersionUID = 1L;

    private static final String MSG_ID_EMPTY = "A node identifier must be supplied.";

    private static final String MSG_NODE_DUPLICATE = "Tree already has node with id";

    private static final String MSG_NODE_DUP_PARENT = "Parent and child node are the same";

// -----------------------------------------------------------------------------
// Member variables.
// -----------------------------------------------------------------------------
   /**
    * Hash table of nodes.
    */
    private Map<String,SingleParentNode> mapNodes = new HashMap<String,SingleParentNode>();

// -----------------------------------------------------------------------------
// Constructors.
// -----------------------------------------------------------------------------
    /**
     * Default constructor.
     */
    public SingleParentTree() {
    }

// -----------------------------------------------------------------------------
// Methods.
// -----------------------------------------------------------------------------
   /**
    * Format object for debug.
    */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder( 1000 );
        sb.append( "SingleParentTree" );
        Set<Entry<String, SingleParentNode>> setEntry = mapNodes.entrySet();
        setEntry.forEach( entry -> {
            sb.append( "\n   " ).append( entry );
        });
        return sb.toString();
    }

   /**
    * Clear out tree.
    */
    public void clear() {
        Set<Entry<String, SingleParentNode>> setEntry = mapNodes.entrySet();
        setEntry.forEach( entry -> {
            SingleParentNode node = entry.getValue();
            node.clear();
        });
        mapNodes.clear();
        mapNodes = new HashMap<String, SingleParentNode>();
    }

    /**
     * Check id supplied.
     * @param id    ID to check.
     * @throws NodeCreationException if null/empty.
     */
    protected void checkId( final String id )
    throws NodeCreationException
    {
        if( id == null || id.length() < 1 ) {
            throw new NodeCreationException( MSG_ID_EMPTY );
        }
    }

    /**
     * Format id into builder.
     * @param sb    Builder.
     * @param id    Id.
     * @return Builder.
     */
    private StringBuilder formatId(
            final StringBuilder sb,
            final String id )
    {
        return sb.append( " [" ).append( id ).append( "]" );
    }
    /**
     * Check for duplicate node.
     * @param id    ID to check.
     * @throws DuplicateNodeException if duplicate
     */
    protected void checkDuplicate( final String id )
    throws DuplicateNodeException {
        if( mapNodes.containsKey( id ) ) {
            StringBuilder sb = new StringBuilder( MSG_NODE_DUPLICATE );
            formatId( sb, id ).append( "." );
            throw new DuplicateNodeException( sb.toString() );
        }
    }

    /**
     * Check for duplicate node.
     * @param id    ID to check.
     * @throws DuplicateNodeException if duplicate
     */
    protected void checkDuplicateParent( final String id )
    throws DuplicateNodeException
    {
        if( mapNodes.containsKey( id ) ) {
            StringBuilder sb = new StringBuilder( MSG_NODE_DUP_PARENT );
            formatId( sb, id ).append( "." );
            throw new DuplicateNodeException( sb.toString() );
        }
    }

    /**
     * Format not found exception for parent/child.
     * @param parentID  ID for parent.
     * @param childID   ID for child.
     * @throws NodeNotFoundException
     */
    private void checkNodeParentChild(
            final String parentID,
            final String childID )
    throws NodeNotFoundException
    {
        StringBuilder sb = new StringBuilder( 200 );
        sb.append( "Existing parent (id " );
        formatId( sb, parentID );
        sb.append( ") for this child (id " );
        formatId( sb, childID );
        sb.append( ") was not found in tree." );
        throw new NodeNotFoundException( sb.toString() );
    }

   /**
    * Factory method that creates a node with specified id and name.
    * @param id   Node identifier; may not be null or empty.
    * @param name Node name.
    * @return Initialized node.
    * @exception NodeCreationException if error creating node.
    */
    public SingleParentNode createNode(
           final String id,
           final String name )
    throws NodeCreationException
    {
        checkId( id );
        return new SingleParentNode( id, name );
    }

   /**
    * Factory method that creates a node with specified id and name.
    * @param id   Node identifier; may not be null or empty.
    * @return Initialized node.
    * @exception NodeCreationException if error creating node.
    */
    public SingleParentNode createNode( final String id )
    throws NodeCreationException
    {
        checkId( id );
        return new SingleParentNode( id );
    }

   /**
    * Add node into the tree's collection.
    * @param node Node.
    * @exception DuplicateNodeException if attempting to add duplicate node.
    */
    public void addNode( final SingleParentNode node )
    throws DuplicateNodeException
    {
        if( node == null ) return;

        String id = node.getID();
        if( id == null ) return;

        checkDuplicate( id );
        mapNodes.put( id, node );
    }

   /**
    * Add child node.
    * @param child Child node.
    * @exception DuplicateNodeException if attempting to add duplicate node.
    * @exception CyclicLoopException if cyclic error.
    */
    public void addChild(
            final SingleParentNode parent,
            final SingleParentNode child )
    throws DuplicateNodeException, CyclicLoopException
    {
        if( parent == null ) return;
        if( child == null ) return;

        String id = child.getID();
        if( id == null ) return;

        SingleParentNode existingNode = mapNodes.get( id );
        if( existingNode == null )
        {
            mapNodes.put( id, child );
        }
        else {
            checkDuplicate( id );
        }

        // Add child to parent
        parent.addChild( child );
    }

   /**
    * Set parent for specified child node.
    * @param parentID Parent node ID.
    * @param childID  Child node ID.
    * @exception CyclicLoopException if cyclic error.
    * @exception DuplicateNodeException if parent and child are the same.
    * @exception NodeNotFoundException if parent node could not be found.
    */
    public void makeParent(
            final String parentID,
            final String childID )
    throws CyclicLoopException, DuplicateNodeException, NodeNotFoundException
    {
        if( parentID == null ) return;
        if( childID == null ) return;

        if( parentID.equals( childID ) ) {
            checkDuplicateParent( childID );
        }

        SingleParentNode parent = mapNodes.get( parentID );
        SingleParentNode child = mapNodes.get( childID );

        if( parent == null ) return;
        if( child == null ) return;

        SingleParentNode oldParent = child.getParentNode();
        if( oldParent != null ) {
            String key = oldParent.getID();
            if( ! mapNodes.containsKey( key ) ) {
                checkNodeParentChild( key, childID );
            }
        }

        if( child._parentNode != null ) {
            oldParent.removeChild( childID );
        }
        parent.makeParent( child );
    }

   /**
    * Fetch a node from the tree.
    * @param id ID of node to retrieve.
    * @return Node, or <i>null</i> if not found.
    */
    public SingleParentNode fetchNode( final String id )
    {
        return mapNodes.get( id );
    }

   /**
    * Test whether node is in the tree.
    * @param id ID of node to test.
    * @return <i>true</i> if node is in tree.
    */
    public boolean checkNode( final String id )
    {
        if( id == null ) return false;
        return mapNodes.containsKey( id );
    }

   /**
    * Return a list of child nodes for specified ID.
    * @param  id ID of node.
    * @return List of children.
    */
    public List<SingleParentNode> fetchChildNodes( final String id )
    {
        List<SingleParentNode> children = null;
        SingleParentNode node = fetchNode( id );
        if( node != null )
        {
            children = node.getChildren();
        }
        return children;
    }

   /**
    * Build list of nodes from the tree by loading nodes from from specified
    * parent node into the list.
    *
    * @param nodeList List of nodes to update; nodes are appended to this list.
    * @param parent   Parent node to process.
    */
    private void fetchAllNodes(
            final List<SingleParentNode> nodeList,
            final SingleParentNode parent )
    {
        List<SingleParentNode> children = parent.getChildren();
        if( children != null ) {
            children.forEach( child -> {
                nodeList.add( child );

                // Process each child
                fetchAllNodes( nodeList, child );
            });
        }
    }

   /**
    * Build list of nodes that represents a sub-tree of nodes from specified
    * parent node. Nodes are returned by performing a pre-order tree traversal.
    * The node is visited first and then each child is visited recursively.
    *
    * @param parent  Parent node to process.
    * @return List of nodes to process.
    */
    public List<SingleParentNode> fetchAllNodes( final SingleParentNode parent )
    {
        List<SingleParentNode> nodeList = new ArrayList<SingleParentNode>();
        if( parent != null ) {
            nodeList.add( parent );
            fetchAllNodes( nodeList, parent );
        }
        return nodeList;
    }

   /**
    * Build list of nodes that represents a sub-tree of nodes from specified
    * parent node. Nodes are returned by performing a pre-order tree traversal.
    *
    * @param node        Node to process.
    * @param includeSelf Include specified node in list.
    * @return List of nodes in sub-tree.
    */
    public List<SingleParentNode> fetchNodesSubTree(
            final SingleParentNode node,
            final boolean includeSelf )
    {
        List<SingleParentNode> nodeList = new ArrayList<SingleParentNode>();
        if( node != null )
        {
            if( includeSelf ) nodeList.add( node );
            fetchAllNodes( nodeList, node );
        }
        return nodeList;
    }

   /**
    * Build list of nodes that represents a sub-tree of nodes from specified
    * parent node. Nodes are returned by performing a pre-order tree traversal.
    * The node is visited first and then each child is visited recursively.
    *
    * @param nodeID ID of parent node to process.
    * @return List of nodes to process.
    */
    public List<SingleParentNode> fetchAllNodes( final String nodeID ) {
        SingleParentNode parent = fetchNode( nodeID );
        List<SingleParentNode> nodeList = new ArrayList<SingleParentNode>();
        if( parent != null ) {
            nodeList.add( parent );
            fetchAllNodes( nodeList, parent );
        }
        return nodeList;
    }

   /**
    * Sort nodes in sub-tree.
    * @param parent Parent node to process.
    */
    public void sortNodes( final SingleParentNode parent )
    {
        List<SingleParentNode> children = parent.getChildren();
        if( children != null ) {
            Collections.sort( children );
            children.forEach( child -> {
                sortNodes( child );
            });
        }
    }

   /**
    * Remove specified node from tree's map. The node is removed from the tree.
    * All children are cleared from the node. The node object is not deleted.
    *
    * @param node   Node to remove.
    */
    public void delete( final SingleParentNode node ) {
        if( node != null ) {
            String key = node.getID();
            if( key != null ) {
               // Remove from map
               mapNodes.remove( key );
            }

            // Clear children pointers for each child.
            List<SingleParentNode> listChildren = node.getChildren();
            SingleParentNode.clearParents( listChildren );

            // Remove from parent's children
            SingleParentNode nodeParent = node.getParentNode();
            if( nodeParent != null ) nodeParent.removeChild( key );

            // Clear the children of node
            node.clearChildren();
        }
    }

   /**
    * Prune specified parent node and all children out of the tree. The node
    * objects are not deleted.
    *
    * @param node  Parent node to process.
    */
    public void pruneTree( final SingleParentNode node )
    {
        List<SingleParentNode> nodeList = fetchNodesSubTree( node, false );
        for( SingleParentNode nodeDep : nodeList )
        {
            String key = nodeDep.getID();

            // Clear out dependant children list
            node.clear();

            // Remove from map
            mapNodes.remove( key );
        }

        // Remove node from parent
        String key = node.getID();
        SingleParentNode nodeParent = node.getParentNode();
        if( nodeParent != null ) nodeParent.removeChild( key );

        // Now remove node from tree
        mapNodes.remove( key );
    }

   /**
    * Return a list of all nodes in the tree. Nodes are not returned in any
    * special sequence.
    *
    * @return List of nodes, or empty list if none.
    */
    public List<SingleParentNode> fetchAllNodes()
    {
        List<SingleParentNode> listNodes = new ArrayList<SingleParentNode>();
        Set<Entry<String, SingleParentNode>> setEntry = mapNodes.entrySet();
        setEntry.forEach( entry -> {
            listNodes.add( entry.getValue() );
        });
        return listNodes;
    }

   /**
    * Return a list of all root nodes in the tree. Nodes are not returned in any
    * special sequence.
    * @return List of nodes, or empty list if none.
    */
    public List<SingleParentNode> fetchAllRootNodes() {
        List<SingleParentNode> listNodes = new ArrayList<SingleParentNode>();
        Set<Entry<String, SingleParentNode>> setEntry = mapNodes.entrySet();
        setEntry.forEach( entry -> {
            SingleParentNode node = entry.getValue();
            if( node.getParentNode() == null ) {
                listNodes.add( node );
            }
        });
        return listNodes;
    }

   /**
    * Add nodes into tree that represents the specified parent-child
    * relationship; new nodes will be created and linked.
    *
    * @param parentID Parent node ID.
    * @param childID  Child node ID.
    * @return Parent node.
    * @exception NodeCreationException if error creating node.
    * @exception DuplicateNodeException if attempting to add duplicate node.
    * @exception CyclicLoopException if cyclic error.
    */
    public SingleParentNode addParentChild(
           final String parentID,
           final String childID )
    throws NodeCreationException, DuplicateNodeException, CyclicLoopException
    {
        if( parentID == null ) return null;
        if( childID == null ) return null;

        if( parentID.equals( childID ) ) {
            checkDuplicateParent( childID );
        }

        SingleParentNode parent = mapNodes.get( parentID );
        if( parent == null ) {
           parent = createNode( parentID );
           addNode( parent );
        }

        SingleParentNode child = mapNodes.get( childID );
        if( child == null ) {
           child = createNode( childID );
        }

        addChild( parent, child );
        return parent;
   }

    /**
     * Remove parent for specified child node.
     * @param parentID Parent node ID.
     * @param childID  Child node ID.
     * @exception DuplicateNodeException if parent and child are the same.
     * @exception NodeNotFoundException if parent node could not be found.
     */
     public void removeParent(
            final String parentID,
            final String childID )
     throws DuplicateNodeException, NodeNotFoundException
     {
         if( parentID == null ) return;
         if( childID == null ) return;

         if( parentID.equals( childID ) ) {
             checkDuplicateParent( childID );
         }

         SingleParentNode parent = mapNodes.get( parentID );
         SingleParentNode child = mapNodes.get( childID );

         if( parent == null ) return;
         if( child == null ) return;

         SingleParentNode oldParent = child.getParentNode();
         if( oldParent != null ) {
             String key = oldParent.getID();
             if( ! mapNodes.containsKey( key ) ) {
                 checkNodeParentChild( key, childID );
             }
         }

         // Remove child from parent
         if( child._parentNode != null ) {
             oldParent.removeChild( childID );
         }

         // Remove parent from child.
         child.clearParent();
     }

   /**
    * Unroll tree into a list with using pre-order traversal.
    * @param tree Single parent tree.
    * @return List of tree nodes.
    */
    public static List<SingleParentNode> unrollTree( final SingleParentTree tree ) {
        List<SingleParentNode> list = new ArrayList<SingleParentNode>();
        List<SingleParentNode> listRootNodes = tree.fetchAllRootNodes();
        for( Object objRoot : listRootNodes )
        {
            SingleParentNode node = ( SingleParentNode ) objRoot;
            list.add( node );
            unrollNode( list, node );
        }
        return list;
    }

   /**
    * Unroll tree node into specified list.
    * @param list List of nodes build.
    * @param node Node to process.
    */
    private static void unrollNode(
            final List<SingleParentNode> list,
            final SingleParentNode node )
    {
        List<SingleParentNode> listChildren = node.getChildren();
        if( listChildren == null ) return;
        for( Object objChild : listChildren )
        {
            SingleParentNode nodeChild = ( SingleParentNode ) objChild;
            list.add( nodeChild );
            unrollNode( list, nodeChild );
        }
    }

   /**
    * Order nodes in tree using default sequence comparator.
    * @param tree Single parent tree.
    */
    public static void sort( final SingleParentTree tree )
    {
        TreeNodeSequenceComparator comparator = new TreeNodeSequenceComparator();
        List<SingleParentNode> listRootNodes = tree.fetchAllRootNodes();
        if( listRootNodes == null ) return;
        Collections.sort( listRootNodes, comparator );
        for( Object objRoot : listRootNodes ) {
            SingleParentNode node = ( SingleParentNode ) objRoot;
            sortNode( node, comparator );
        }
    }

    /**
     * Order node in tree using specified comparator.
     * @param node       Single parent tree node.
     * @param comparator Comparator.
     */
     private static void sortNode(
             final SingleParentNode node,
             final TreeNodeSequenceComparator comparator )
     {
         List<SingleParentNode> listChildNodes = node.getChildren();
         if( listChildNodes == null ) return;
         Collections.sort( listChildNodes, comparator );
         for( Object objRoot : listChildNodes )
         {
             SingleParentNode nodeChild = ( SingleParentNode ) objRoot;
             sortNode( nodeChild, comparator );
         }
     }


//   /**
//    * Order nodes in tree using specified comparator.
//    * @param tree       Single parent tree.
//    * @param comparator Comparator.
//    */
//    public static void sort(
//            final SingleParentTree tree,
//            final Comparator<?> comparator )
//    {
//        List<SingleParentNode> listRootNodes = tree.fetchAllRootNodes();
//        if( listRootNodes == null ) return;
//        Collections.sort( listRootNodes, comparator );
//        for( Object objRoot : listRootNodes )
//        {
//            SingleParentNode node = ( SingleParentNode ) objRoot;
//            sortNode( node, comparator );
//        }
//    }
//
//   /**
//    * Order node in tree using specified comparator.
//    * @param node       Single parent tree node.
//    * @param comparator Comparator.
//    */
//    private static void sortNode(
//            final SingleParentNode node,
//            final Comparator<?> comparator )
//    {
//        List<SingleParentNode> listChildNodes = node.getChildren();
//        if( listChildNodes == null ) return;
//        Collections.sort( listChildNodes, comparator );
//        for( Object objRoot : listChildNodes )
//        {
//            SingleParentNode nodeChild = ( SingleParentNode ) objRoot;
//            sortNode( nodeChild, comparator );
//        }
//    }

// -----------------------------------------------------------------------------
// Properties.
// -----------------------------------------------------------------------------

}

// -----------------------------------------------------------------------------
// End of Source.
// -----------------------------------------------------------------------------
//
