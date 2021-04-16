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
 * Copyright � 2005 Thomson MICROMEDEX.  All Rights Reserved.
 * Created on Apr 19, 2005
 */
package com.ibm.wh.siam.common.tree;

/**
 * Defines common behavior and properties of a tree node.
 *
 * @author Match Grun
 *
 */
public abstract class TreeNode
implements Comparable<Object>, java.io.Serializable
{
// -----------------------------------------------------------------------------
// Constants.
// -----------------------------------------------------------------------------
    /**
     * Serialized ID.
     */
    private static final long serialVersionUID = 1L;

// -----------------------------------------------------------------------------
// Member variables.
// -----------------------------------------------------------------------------
    /**
     * ID.
     */
    protected String id = null;

    /**
     * Node type.
     */
    protected String type = null;

    /**
     * Display name.
     */
    protected String text = null;

    /**
     * Object.
     */
    protected Object object = null;

    /**
     * Sequence.
     */
    protected int sequence = 0;

// -----------------------------------------------------------------------------
// Constructors.
// -----------------------------------------------------------------------------
    /**
     * Default constructor.
     */
    public TreeNode() {
    }

    /**
     * Alternate constructor that specifies node ID.
     * @param id ID.
     */
    public TreeNode( final String id ) {
       this.setID( id );
    }

    /**
     * Alternate constructor that specifies node ID and text to load.
     * @param id   ID.
     * @param text Text to load into node.
     */
    public TreeNode( final String id,
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
    public String toString() {
       StringBuilder sb = new StringBuilder( 60 );
       String clsName = this.getClass().getName();
       int pos = clsName.lastIndexOf( "." );
       sb.append( clsName.substring( 1 + pos ) );
       sb.append( " id/typ/load/text/seq/obj: " );
       sb.append( id ).append( "/" );
       sb.append( type ).append( "/" );
       sb.append( text ).append( "/" );
       sb.append( sequence ).append( "/" );
       sb.append( object );
       return sb.toString();
    }

    /**
     * Comparison function for sorting TreeNode objects. The node's
     * text // ID will be used as the sort key.
     *
     * @param obj Object being compared against.
     * @return Negative, zero, position integer.
     * @see Comparable
     * @exception ClassCastException if objects are not TreeNode objects.
     */
    @Override
    public int compareTo( final Object obj )
    throws ClassCastException
    {
       int retVal = 0;
       if( obj instanceof TreeNode )
       {
          TreeNode node = ( TreeNode ) obj;
          retVal = text.compareTo( node.text );
          if( retVal == 0 )
          {
             retVal = id.compareTo( node.id );
          }
       }
       else
       {
          throw new ClassCastException( "Object must be a TreeNode." );
       }
       return retVal;
    }

// -----------------------------------------------------------------------------
// Properties.
// -----------------------------------------------------------------------------
    /**
     * Return ID.
     * @return ID, or <i>null</i> if none specified.
     */
    public String getID() {
        return id;
    }

    /**
     * Specify ID.
     * @param value ID.
     */
    public void setID( final String value ) {
        id = value;
    }

    /**
     * Return node type.
     * @return ID, or <i>null</i> if none specified.
     */
    public String getType() {
        return type;
    }

    /**
     * Specify text.
     * @param value Text.
     */
    public void setType( final String value ) {
        type = value;
    }

    /**
     * Return text.
     * @return ID, or <i>null</i> if none specified.
     */
    public String getText() {
        return text;
    }

    /**
     * Specify text.
     * @param value Text.
     */
    public void setText( final String value ) {
        text = value;
    }

    /**
     * Return Object.
     * @return ID, or <i>null</i> if none specified.
     */
    public Object getObject() {
        return object;
    }

    /**
     * Specify Object.
     * @param value Object.
     */
    public void setObject( final Object value ) {
        object = value;
    }

    /**
     * Return sequence.
     */
    public int getSequence() {
        return sequence;
    }

    /**
     * Specify sequence.
     * @param value Sequence.
     */
    public void setSequence( final int value ){
        sequence = value;
    }

}

// -----------------------------------------------------------------------------
// End of Source.
// -----------------------------------------------------------------------------
//
