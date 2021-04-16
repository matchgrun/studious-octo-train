/********************************************************* {COPYRIGHT-TOP} ***
* IBM Confidential
* OCO Source Materials
* *** thidsutilities ***
*
* (C) Copyright IBM Corporation 2018
*
* The source code for this program is not published or otherwise
* divested of its trade secrets, irrespective of what has been
* deposited with the U.S. Copyright Office.
********************************************************* {COPYRIGHT-END} **/
/*
 * Class Name:  SingleParentNode
 * Description: Defines a tree node that may only have a single parent.
 *
 * Copyright � 2005 Thomson MICROMEDEX.  All Rights Reserved.
 * Created on Apr 14, 2005
 */
package com.ibm.wh.siam.common.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * Defines a tree node that may only have a single parent. Note that these
 * objects are used with a SingleParentTree. The <code>setID()</code> method
 * should not be used by the application when the node is part of a
 * SingleParentTree. This may compromise the integrity of the tree if such
 * nodes are modified; these nodes should be managed through the tree.
 * </p>
 *
 * @author Match Grun
 */
public class SingleParentNode
extends TreeNode
implements java.io.Serializable
{
// -----------------------------------------------------------------------------
// Constants.
// -----------------------------------------------------------------------------
    /**
     * Serialized ID.
     */
    private static final long serialVersionUID = 1L;

    private static final String MSG_CYCLIC = "Node ancestor(s) already have node with id";

    private static final String MSG_CLASSCAST = "Object must be a SingleParentNode.";

// -----------------------------------------------------------------------------
// Member variables.
// -----------------------------------------------------------------------------
    /**
     * Indent level.
     */
    protected int _level = 0;

    /**
     * Reference to parent node.
     */
    protected SingleParentNode _parentNode = null;

    /**
     * Children.
     */
    protected List<SingleParentNode> _children = null;

// -----------------------------------------------------------------------------
// Constructors.
// -----------------------------------------------------------------------------
    /**
     * Default constructor.
     */
    public SingleParentNode() {
    }

    /**
     * Alternate constructor that specifies node ID and text to load.
     * @param id   ID.
     */
    public SingleParentNode( final String id ) {
       this.setID( id );
    }

    /**
     * Alternate constructor that specifies node ID and text to load.
     * @param id   ID.
     * @param text Text to load into node.
     */
    public SingleParentNode(
            final String id,
            final String text )
    {
       this.setID( id );
       this.setText( text );
    }

// -----------------------------------------------------------------------------
// Methods.
// -----------------------------------------------------------------------------
    /**
     * Format object for debug.
     * @return Formatted string.
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder( 60 );
        String clsName = this.getClass().getName();
        int pos = clsName.lastIndexOf( "." );
        sb.append( clsName.substring( 1 + pos ) );
        sb.append( " id/pid/typ/lvl/text/seq/child/obj: " );
        sb.append( id ).append( "/" );
        if( _parentNode == null ) {
            sb.append( "-" );
        }
        else {
            sb.append( _parentNode.getID() );
        }
        sb.append( "/" );
        sb.append( type ).append( "/" );
        sb.append( _level ).append( "/" );
        sb.append( text ).append( "/" );
        sb.append( sequence ).append( "/" );
        sb.append( "children: " );
        sb.append( haveChildren() );
        sb.append( "{" );
        if( _children == null ) {
            sb.append( "-" );
        }
        else {
            boolean flag = false;
            for( Iterator<SingleParentNode> it = _children.iterator(); it.hasNext(); ) {
                SingleParentNode child = it.next();
                if( flag ) sb.append( "," );
                sb.append( child.getID() );
                flag = true;
            }
        }
        sb.append( "}/" );
        sb.append( object );

        return sb.toString();
    }

    /**
     * Test whether node has children.
     * @return <i>true</i> if node has children.
     */
    public boolean haveChildren() {
        return ( countChildren() > 0 );
    }

    /**
     * Return count of children.
     * @return Number of children.
     */
    public int countChildren() {
        int cnt = 0;
        if( _children != null ) {
            cnt = _children.size();
        }
        return cnt;
    }

    /**
     * Clear children nodes.
     */
    public void clearChildren() {
        if( _children != null ) {
           _children.clear();
        }
        _children = null;
    }

    /**
     * Clear nodes.
     */
    public void clear() {
        clearChildren();
        object = null;
    }

    /**
     * Clear parents for each node in list.
     * @param listNodes List of SingleParentNodes.
     */
    public static void clearParents( final List<SingleParentNode> listNodes ) {
        listNodes.forEach( node -> {
            node._parentNode = null;
        });
    }

    /**
     * Comparison function for sorting SingleParentNode objects. The node's
     * text // ID will be used as the sort key.
     *
     * @param obj Object being compared against.
     * @return Negative, zero, position integer.
     * @see Comparable
     * @exception ClassCastException if objects are not SingleParentNode objects.
     */
    @Override
    public int compareTo( final Object obj )
    throws ClassCastException
    {
        int retVal = 0;
        if( obj instanceof SingleParentNode ) {
            SingleParentNode node = ( SingleParentNode ) obj;
            retVal = text.compareTo( node.text );
            if( retVal == 0 ) {
                retVal = id.compareTo( node.id );
            }
        }
        else {
            throw new ClassCastException( MSG_CLASSCAST );
        }
        return retVal;
    }

    /**
     * Test ancestor nodes of node for duplicate ID.
     * @param nodeID Node ID to test.
     * @return <i>true</i> if there is a parent with the same ID as node.
     */
    public boolean checkAncestorID( final String nodeID )
    {
       boolean duplicateID = false;
       SingleParentNode parent = _parentNode;
       while( parent != null ) {
           if( parent.getID().equals( nodeID ) ) {
               duplicateID = true;
               break;
           }
           parent = parent._parentNode;
       }
       return duplicateID;
    }

    /**
     * Format cyclic loop exception.
     * @param id    ID to format.
     * @throws CyclicLoopException Exception.
     */
    private void checkCyclic( final String id )
    throws CyclicLoopException
    {
        StringBuilder sb = new StringBuilder( MSG_CYCLIC );
        sb.append( " [" ).append( id ).append( "]." );
        throw new CyclicLoopException( sb.toString() );
    }

    /**
     * Add child node.
     * @param child Child node.
     * @exception CyclicLoopException if cyclic error.
     */
    void addChild( final SingleParentNode child )
    throws CyclicLoopException
    {
        if( child == null ) return;
        if( checkAncestorID( child.getID() ) ) {
            checkCyclic( child.getID() );
        }
        if( _children == null ) _children = new ArrayList<SingleParentNode>();
        _children.add( child );
        child.setLevel( 1 + _level );
        child._parentNode = this;
    }

    /**
     * Remove node with specified from this node's list of children.
     * @param id ID of node to remove.
     */
    void removeChild( final String id ) {
        if( id == null ) return;
        if( _children == null ) return;
        for( Iterator<SingleParentNode> it = _children.iterator(); it.hasNext(); ) {
          SingleParentNode node = it.next();
          if( node.getID().equals( id ) ) {
             node._parentNode = null;
             node.setLevel( 0 );
             _children.remove( node );
             break;
          }
       }
    }

    /**
     * Make current node parent of specified child node.
     * @param child Child node.
     * @exception CyclicLoopException if cyclic error.
     */
    void makeParent( final SingleParentNode child )
    throws CyclicLoopException
    {
        if( child == null ) return;
        if( checkAncestorID( child.getID() ) ) {
            checkCyclic( child.getID() );
        }
        if( _children == null ) _children = new ArrayList<SingleParentNode>();
        _children.add( child );
        child.setLevel( 1 + _level );
        child._parentNode = this;
    }

    /**
     * Return ancestry of current node.
     * @return List of nodes.
     */
    public List<SingleParentNode> ancestry() {
        List<SingleParentNode> ancesters = new ArrayList<SingleParentNode>();
        SingleParentNode parent = _parentNode;
        while( parent != null ) {
            ancesters.add( parent );
            parent = parent._parentNode;
        }
        return ancesters;
    }

    /**
     * Clear parent for this node.
     */
    public void clearParent() {
       if( _parentNode != null ) {
           _parentNode.removeChild( id );
       }
       _parentNode = null;
    }

// -----------------------------------------------------------------------------
// Properties.
// -----------------------------------------------------------------------------
    /**
     * Return indent level.
     * @return Level.
     */
    public int getLevel() {
        return _level;
    }

    /**
     * Specify indent level. The level is incremented for a child when the child
     * is added and reset to zero for a child when the child is removed. It is
     * possible that the level gets out of sync to the tree structure if the tree
     * is built in random order (ie, not from top down).
     *
     * @param value Level.
     */
    public void setLevel( final int value ) {
        _level = value;
    }

    /**
     * Return Object attached to node.
     * @return ID, or <i>null</i> if none specified.
     */
    @Override
    public Object getObject() {
        return object;
    }

    /**
     * Specify Object to attach to node.
     * @param value Object.
     */
    @Override
    public void setObject( final Object value ) {
        object = value;
    }

    /**
     * Return reference to parent node.
     * @return Parent node, or <i>null</i> if none.
     */
    public SingleParentNode getParentNode() {
        return _parentNode;
    }

    /**
     * Return children of node.
     * @return Children, or <i>null</i> if none added.
     */
    public List<SingleParentNode> getChildren() {
        return _children;
    }

}

// -----------------------------------------------------------------------------
// End of Source.
// -----------------------------------------------------------------------------
//
