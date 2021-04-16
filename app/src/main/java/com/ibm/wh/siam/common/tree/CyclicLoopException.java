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
 * Class Name:  CyclicLoopException
 * Description: Defines an exception that is caused when attempting to create
 *              that would result in a cyclic loop in the tree heirarchy.
 *
 * Copyright � 2005 Thomson MICROMEDEX.  All Rights Reserved.
 * Created on Apr 22, 2005
 */
package com.ibm.wh.siam.common.tree;

/**
 * <p>
 * Defines an exception that is caused when attempting to create that would
 * result in a cyclic loop in the tree hierarchy. This exception would be
 * thrown when attempting to create a node that would result in the same node
 * with the same ID in the node's ancestors.
 * </p>
 *
 * @author Match Grun
 *
 */
public class CyclicLoopException
extends TreeException
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

// -----------------------------------------------------------------------------
// Constructors.
// -----------------------------------------------------------------------------
   /**
    * Default constructor.
    *
    */
    public CyclicLoopException() {
        super();
    }

   /**
    * Creates an exception with a message.
    *
    * @param message The exception message.
    */
    public CyclicLoopException( final String message ) {
        super( message );
    }

   /**
    * Creates an exception with a nested exception.
    *
    * @param cause The nested exception
    */
    public CyclicLoopException( final Exception cause ) {
        super( cause );
    }

   /**
    * Creates an Exception with a message and a nested exception.
    *
    * @param message The exception message.
    * @param cause   The nested exception.
    */
    public CyclicLoopException(
           final String message,
           final Exception cause ) {
        super( message, cause );
    }

// -----------------------------------------------------------------------------
// Methods.
// -----------------------------------------------------------------------------

// -----------------------------------------------------------------------------
// Properties.
// -----------------------------------------------------------------------------

}

// -----------------------------------------------------------------------------
// End of Source.
// -----------------------------------------------------------------------------
//
